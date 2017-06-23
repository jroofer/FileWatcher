/*
 *
 *    Copyright (C) Marcel Meyer- All Rights Reserved
 *  *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  *  Proprietary and confidential
 *  *  Written by Marcel Meyer, devbeg@gmx.net, 2017
 *
 */

package filewatcher.builder;

import filewatcher.api.IFileWatcherManager;
import filewatcher.api.IFileWatcher;
import javafx.util.Pair;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Marcel on 18.06.2017. The implementation of {@link IFileWatcherManager} which provides
 * functions to manage {@link IFileWatcher} instances
 */
public class FileWatcherManager implements IFileWatcherManager
{
  private final Map<Path, Pair<FileWatcherConfiguration, IFileWatcher>> iPathPairMap;

  /**
   * Private constructor for {@link IFileWatcherManager}. Use static factory methods instead!
   */
  private FileWatcherManager()
  {
    iPathPairMap = new HashMap<>();
  }

  /**
   * Static factory method to create a new {@link IFileWatcherManager}
   * 
   * @return The new instance of {@link IFileWatcherManager}
   */
  public static IFileWatcherManager newFileWatcherManager()
  {
    return new FileWatcherManager();
  }

  @Override
  public IFluentBuilder.IStartingBuilder addFileWatcher() throws IOException
  {
    IFluentBuilder.IStartingBuilder tBuilder = IFileWatcherFactory.newFluentBuilder(this);

    return tBuilder;
  }

  @Override
  public MANAGER_RESPONSE isWatching(final Path aPath)
  {
    MANAGER_RESPONSE tBack;

    if (iPathPairMap.containsKey(aPath) == false)
    {
      tBack = MANAGER_RESPONSE.NO_WATCHER_FOUND;
    }
    else
    {
      final Pair<FileWatcherConfiguration, IFileWatcher> tFileWatcherConfigurationIFileWatcherPair =
        iPathPairMap.get(aPath);

      final IFileWatcher tValue = tFileWatcherConfigurationIFileWatcherPair.getValue();

      if (tValue == null)
      {
        tBack = MANAGER_RESPONSE.MISSING_FILE_WATCHER;
      }
      else
      {
        tBack = tValue.isRunning() ? MANAGER_RESPONSE.WATCHING : MANAGER_RESPONSE.NOT_WATCHING;
      }
    }

    return tBack;
  }

  @Override
  public MANAGER_RESPONSE stopWatching(final Path aPath)
  {
    MANAGER_RESPONSE tBack;

    if (isWatching(aPath) != MANAGER_RESPONSE.WATCHING)
    {
      tBack = MANAGER_RESPONSE.NO_WATCHER_FOUND;
    }
    else
    {
      final Optional<IFileWatcher> tFileWatcher = getFileWatcher(aPath);
      final IFileWatcher tIFileWatcher = tFileWatcher.orElseThrow(IllegalStateException::new);
      tIFileWatcher.stop();

      tBack = MANAGER_RESPONSE.NOT_WATCHING;
    }

    return tBack;
  }

  @Override public MANAGER_RESPONSE startWatching(final Path aPath) throws IOException
  {
    MANAGER_RESPONSE tBack = isWatching(aPath);

    if (tBack != MANAGER_RESPONSE.NO_WATCHER_FOUND && tBack != MANAGER_RESPONSE.WATCHING)
    {
      IFileWatcher tIFileWatcher = getFileWatcher(aPath).orElseThrow(IllegalStateException::new);
      tIFileWatcher.start();

      tBack = MANAGER_RESPONSE.WATCHING;
    }

    return tBack;
  }

  @Override
  public Map<Path, MANAGER_RESPONSE> stopAll()
  {
    Map<Path, MANAGER_RESPONSE> tBack = new HashMap<>();

    iPathPairMap.keySet().forEach((tPath) -> tBack.put(tPath, stopWatching(tPath)));

    return tBack;
  }

  @Override
  public Map<Path, MANAGER_RESPONSE> startAll()
  {
    Map<Path, MANAGER_RESPONSE> tBack = new HashMap<>();

    iPathPairMap.keySet().forEach((tPath) -> {
      try
      {
        tBack.put(tPath, startWatching(tPath));
      }
      catch (IOException aE)
      {
        tBack.put(tPath, MANAGER_RESPONSE.NOT_WATCHING);
      }
    });

    return tBack;
  }

  @Override
  public MANAGER_RESPONSE deleteFileWatcher(final Path aPath)
  {
    if (isWatching(aPath) == MANAGER_RESPONSE.WATCHING)
    {
      stopWatching(aPath);
    }

    return iPathPairMap.remove(aPath) != null ? MANAGER_RESPONSE.REMOVED : MANAGER_RESPONSE.NOT_REMOVED;
  }

  @Override
  public Collection<Path> getAllWatchedPaths()
  {
    return iPathPairMap.keySet();
  }

  @Override
  public Optional<FileWatcherConfiguration> getWatcherConfiguration(final Path aPath)
  {
    final Pair<FileWatcherConfiguration, IFileWatcher> tFileWatcherConfigurationIFileWatcherPair =
      iPathPairMap.get(aPath);

    Optional<FileWatcherConfiguration> tBack = Optional.empty();

    if (tFileWatcherConfigurationIFileWatcherPair != null)
    {
      tBack =
        Optional.ofNullable(tFileWatcherConfigurationIFileWatcherPair.getKey());
    }

    return tBack;
  }

  @Override
  public Optional<IFileWatcher> getFileWatcher(final Path aPath)
  {
    final Pair<FileWatcherConfiguration, IFileWatcher> tFileWatcherConfigurationIFileWatcherPair =
      iPathPairMap.get(aPath);

    Optional<IFileWatcher> tBack = Optional.empty();

    if (tFileWatcherConfigurationIFileWatcherPair != null)
    {
      tBack =
        Optional.ofNullable(tFileWatcherConfigurationIFileWatcherPair.getValue());
    }

    return tBack;
  }

  @Override
  public Map<Path, Pair<FileWatcherConfiguration, IFileWatcher>> getPathPairMap()
  {
    return iPathPairMap;
  }
}
