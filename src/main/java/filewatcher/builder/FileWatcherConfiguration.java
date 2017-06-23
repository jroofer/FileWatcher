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
import org.apache.log4j.lf5.LogLevel;

import java.time.Period;

/**
 * Created by Marcel on 16.06.2017. The configuration for the {@link IFileWatcherManager}
 */
public class FileWatcherConfiguration
{
  private Notifier<? extends Notification> iNotification;
  private LogLevel iLogLevel;
  private Period iTimout;

  /**
   * Private constructor, use static factory methods instead!
   */
  private FileWatcherConfiguration()
  {
  }

  /**
   * Create new {@link FileWatcherConfiguration.Builder}
   * 
   * @return The {@link FileWatcherConfiguration.Builder}
   */
  public static FileWatcherConfiguration.Builder newBuilder()
  {
    return Builder.createBuilder();
  }

  /**
   * Set a {@link Notifier} which is called when event occurs.
   * 
   * @param aNotification The {@link Notifier} to set
   * @param <T> The type of {@link Notification} to sent
   */
  private <T extends Notification> void setNotification(final Notifier<T> aNotification)
  {
    iNotification = aNotification;
  }

  /**
   * Set the {@link LogLevel} for the {@link filewatcher.api.IFileWatcher}
   */
  private void setLogLevel(final LogLevel aLogLevel)
  {
    iLogLevel = aLogLevel;
  }

  /**
   * Set the timeout {@link Period} for the {@link filewatcher.api.IFileWatcher}
   */
  private void setTimeout(final Period aTimout)
  {
    iTimout = aTimout;
  }

  /**
   * The Builder class to build a new {@link FileWatcherConfiguration}
   */
  public final static class Builder extends AbstractNestedBuilder<FileWatcher.Builder, FileWatcherConfiguration>
  {
    protected FileWatcherConfiguration iFileWatcherConfiguration;

    /**
     * Private constructor. Use static factory methods instead!
     * 
     * @param aFileWatcherConfiguration The {@link FileWatcherConfiguration} to set
     */
    private Builder(FileWatcherConfiguration aFileWatcherConfiguration)
    {
      iFileWatcherConfiguration = aFileWatcherConfiguration;
    }

    /**
     * Create a new {@link Builder} instance
     */
    protected static Builder createBuilder()
    {
      Builder tBuilder = new Builder(new FileWatcherConfiguration());

      return tBuilder;
    }

    /**
     * {@link FileWatcherConfiguration#setTimeout(Period)}
     * 
     * @param aPeriod The {@link Period} to set
     * @return Builder to continue fluent api
     */
    public Builder withTimeout(Period aPeriod)
    {
      iFileWatcherConfiguration.setTimeout(aPeriod);

      return this;
    }

    /**
     * {@link FileWatcherConfiguration#setLogLevel(LogLevel)}
     * 
     * @param aLogLevel The {@link LogLevel} to set
     * @return Builder to continue fluent api
     */
    public Builder withLogLevel(LogLevel aLogLevel)
    {
      iFileWatcherConfiguration.setLogLevel(aLogLevel);
      return this;
    }

    /**
     * {@link FileWatcherConfiguration#setNotification(Notifier)}
     * 
     * @param aNotification The {@link Notifier} to set
     * @return Builder to continue fluent api
     */
    public <T extends Notification> Builder withNotification(Notifier<T> aNotification)
    {
      iFileWatcherConfiguration.setNotification(aNotification);
      return this;
    }

    @Override
    protected FileWatcherConfiguration build()
    {
      // TODO validate
      return iFileWatcherConfiguration;
    }
  }

}
