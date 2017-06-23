/*
 *
 *    Copyright (C) Marcel Meyer- All Rights Reserved
 *  *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  *  Proprietary and confidential
 *  *  Written by Marcel Meyer, devbeg@gmx.net, 2017
 *
 */

package filewatcher.builder;

import filewatcher.api.IEventProcessor;
import filewatcher.api.IFileWatcher;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Marcel on 16.06.2017. Implementation of {@link IFileWatcher} to watch
 * {@link java.io.File}s and directories for specified {@link java.nio.file.WatchEvent.Kind}s
 */
public class FileWatcher implements IFileWatcher
{
  private boolean iInfinite;
  private boolean iRunning;
  private Path iPath;
  private final WatchService iWatchService;
  private WatchEvent.Kind[] iKinds;
  private Map<WatchKey, Path> iPathByKeys;
  private boolean iRecursiv;
  private Set<IEventProcessor> iProcessor;

  /**
   * This constructor is private 'cause the user should use the {@link Builder} instead. Init new
   * FileWatcher, which may throw IOException if the {@link System} does not allow to get
   * {@link WatchService}
   */
  private FileWatcher() throws IOException
  {
    iPathByKeys = new HashMap<>();
    iProcessor = new HashSet<>();
    iWatchService = FileSystems.getDefault().newWatchService();
  }

  /**
   * Set the flag indicating if the IFileWatcher is running. Should not be called from outside, it
   * is used to implement default configure and stop behaviour. Use {@link IFileWatcher#start()} or
   * {@link IFileWatcher#stop()} instead
   *
   * @param aRunning The flag to set
   */
  private void setRunning(final boolean aRunning)
  {
    iRunning = aRunning;
  }

  /**
   * Set the {@link Path} to watch. The {@link Path} must not be modifiable. Create new
   * {@link IFileWatcher} for other {@link Path}
   * 
   * @param aPath The {@link Path} to watch
   */
  private void setPath(final Path aPath)
  {
    iPath = aPath;
  }

  @Override
  public void addProcessor(final IEventProcessor aProcessor)
  {
    iProcessor.add(aProcessor);
  }

  @Override
  public void setKinds(final WatchEvent.Kind[] aKinds)
  {
    iKinds = aKinds;
  }

  @Override
  public Path getWatchedPath()
  {
    return iPath;
  }

  @Override
  public boolean isInfinite()
  {
    return iInfinite;
  }

  @Override
  public void setInfinite(boolean aInfinite)
  {
    iInfinite = aInfinite;
  }

  @Override
  public boolean isRunning()
  {
    return false;
  }

  @Override
  public boolean isRecursiv()
  {
    return iRecursiv;
  }

  @Override
  public void setRecursiv(final boolean aRecursiv)
  {
    iRecursiv = aRecursiv;
  }

  @Override
  public void start() throws IOException
  {
    setRunning(true);
    startWatching();
  }

  @Override
  public void stop()
  {
    setRunning(false);
    iPathByKeys = new HashMap<>();
  }

  /**
   * Start watching path. Includes subdirectories if {@link FileWatcher#isInfinite()}
   *
   * @throws IOException
   */
  private void startWatching() throws IOException
  {
    if (isRecursiv())
    {
      Files.walkFileTree(iPath, new SimpleFileVisitor<Path>()
      {
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException
        {
          register(dir);
          return FileVisitResult.CONTINUE;
        }
      });
    }
    else
    {
      register(iPath);
    }

    new FileWatcherRunnable().run();
  }

  private class FileWatcherRunnable implements Runnable
  {
    @Override
    public void run()
    {
      do
      {
        try
        {
          final WatchKey tTake = iWatchService.take();

          if (tTake.isValid())
          {
            Path tPath = iPathByKeys.get(tTake);

            if (tPath != null)
            {
              // TODO log
              processEvent(tTake.pollEvents());
            }
          }
        }
        catch (InterruptedException aE)
        {
          // TODO log here
        }
      }
      while (iRunning && iInfinite);
    }

    /**
     * Process events with configured {@link IEventProcessor}s
     *
     * @param aWatchEvents The WatchEvents to process
     */
    private void processEvent(final List<WatchEvent<?>> aWatchEvents)
    {
      final List<WatchEvent<Path>> tWatchEvents =
        aWatchEvents.stream().map(event -> IFileWatcher.<Path> castEvent(event)).collect(Collectors.toList());

      synchronized (iProcessor)
      {
        iProcessor.forEach(tProcessor -> tProcessor.processEvents(tWatchEvents));
      }
    }
  }

  /**
   * Start watching the configured path
   *
   * @return True if started, false otherwise
   */
  private void register(Path aPath) throws IOException
  {
    try
    {
      iPathByKeys.put(aPath.register(iWatchService, iKinds), aPath);
    }
    catch (IOException aException)
    {
      // TODO log here
      throw aException;
    }
  }

  public static FileWatcher.Builder newBuilder() throws IOException
  {
    return Builder.createNewBuilder();
  }

  /**********************************
   *********** BUILDER ***************
   ***********************************/

  public final static class Builder extends AbstractNestedBuilder<IFluentBuilder.IFinishingBuilder, FileWatcher>
  {
    private FileWatcher iFileWatcher;

    private Builder(FileWatcher aFileWatcher)
    {
      iFileWatcher = aFileWatcher;
    }

    /**
     * Create new instance of Builder
     *
     * @return The instance of Builder
     * @exception IOException in case the path not exists or is protected
     */
    public static Builder createNewBuilder() throws IOException
    {
      Builder tBuilder = new Builder(new FileWatcher());

      return tBuilder;
    }

    /**
     * Set the {@link Path} to watch
     * @param aPath The {@link Path} to set
     * @return {@link Builder} to continue building the {@link IFileWatcher}
     */
    public Builder watch(Path aPath)
    {
      iFileWatcher.setPath(aPath);
      return this;
    }

    /**
     * Set the {@link java.nio.file.WatchEvent.Kind}s to watch for
     * @param aKinds The {@link java.nio.file.WatchEvent.Kind}s to watch for
     * @return {@link Builder} to continue building the {@link IFileWatcher}
     */
    public Builder forEvents(WatchEvent.Kind... aKinds)
    {
      iFileWatcher.setKinds(aKinds);

      return this;
    }

    /**
     * Set the isInfinite flag {@link FileWatcher#isInfinite()}}
     *
     * @param aInfinite The value for the flag to set
     * @return {@link Builder} to continue building the {@link IFileWatcher}
     */
    public Builder withInfinite(boolean aInfinite)
    {
      iFileWatcher.setInfinite(aInfinite);

      return this;
    }

    /**
     * Set the flag indicating if the IFileWatcher should be started automatically after building it
     *
     * @param aInitialRunning The flag to set
     * @return {@link Builder} to continue building the {@link IFileWatcher}
     */
    public Builder withInitialRunning(boolean aInitialRunning)
    {
      iFileWatcher.setRunning(aInitialRunning);

      return this;
    }

    /**
     * Set the flag indicating if the IFileWatcher should watch recursively which means to include
     * subdirectories
     *
     * @param aRecursiv The flag to set
     * @return {@link Builder} to continue building the {@link IFileWatcher}
     */
    public Builder withRecursiv(boolean aRecursiv)
    {
      iFileWatcher.setRecursiv(aRecursiv);
      return this;
    }

    /**
     * Use the specified {@link IEventProcessor} when event happens. You can call this several times to add more
     * @param aEvenProccesor The {@link IEventProcessor} to use
     * @return {@link Builder} to continue building the {@link IFileWatcher}
     */
    public Builder useProcessor(final IEventProcessor aEvenProccesor)
    {
      iFileWatcher.addProcessor(aEvenProccesor);
      return this;
    }

    /**
     * Get the flag indicating if current configuration is valid enough to call build
     * @return True if and only if the configuration is valid to call build on it
     */
    public boolean isValid()
    {
      return iFileWatcher.iPath != null && iFileWatcher.iProcessor.size() > 0;
    }

    @Override
    protected FileWatcher build()
    {
      if (isValid())
      {
        return iFileWatcher;
      }
      else
      {
        throw new IllegalStateException("Validation was not successful. Please see logs for details!");
      }
    }
  }
}
