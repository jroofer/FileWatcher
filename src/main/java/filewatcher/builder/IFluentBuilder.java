/*
 *
 *    Copyright (C) Marcel Meyer- All Rights Reserved
 *  *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  *  Proprietary and confidential
 *  *  Written by Marcel Meyer, devbeg@gmx.net, 2017
 *
 */

package filewatcher.builder;

import filewatcher.api.IFileWatcher;
import filewatcher.api.IFileWatcherManager;
import javafx.util.Pair;

import java.io.IOException;

/**
 * Created by Marcel on 18.06.2017. The IFluentBuilder interface provides a builder sequence which
 * guides you through the process of creating a valid {@link IFileWatcher}.
 * <br />
 * USAGE: When using this
 * interface to create a new {@link IFileWatcher} you first receive a {@link IStartingBuilder} which
 * has just one function that is used to start the configuration process
 * {@link IStartingBuilder#configure()}. After calling {@link IStartingBuilder#configure()} you
 * receive the builder for the {@link FileWatcherConfiguration}. Use this builder to set all
 * necessary configurations you want to use within your {@link IFileWatcher}. This builder is used
 * to configure meta-configurations. After finishing call
 * {@link FileWatcherConfiguration.Builder#done()} to continue. The FluentBuilder will return the
 * next Builder which is in fact the {@link filewatcher.builder.FileWatcher.Builder}. You have to
 * use this builder to build the {@link IFileWatcher} instance. Be sure to set all mandatory
 * information and continue by calling {@link FileWatcher.Builder#done()}. Each time you call done()
 * method on a Builder your input is validated. If you processed the last builder the
 * {@link IFluentBuilder} returns itself. You can now call {@link Builder#build()} on it to build
 * both the FileWatcherConfiguration and the IFileWatcher.
 */
public interface IFluentBuilder
{
  /**
   * The Builder to use to build the IFileWatcher and the FileWatcherConfiguration
   */
  final class Builder implements IFinishingBuilder, IStartingBuilder
  {
    private FileWatcherConfiguration iWatchConfiguration;
    private IFileWatcher iFileWatcher;

    private IFileWatcherManager iWatcherManager;

    private FileWatcher.Builder iFileWatchBuilder;
    private FileWatcherConfiguration.Builder iFileWatcherConfigurationBuilder;

    /**
     * Default constructor for Builder
     * @param aWatcherManager The {@link IFileWatcherManager} to use
     * @throws IOException
     */
    public Builder(IFileWatcherManager aWatcherManager) throws IOException
    {
      iWatcherManager = aWatcherManager;
      iFileWatchBuilder = FileWatcher.newBuilder().withParentBuilder((IFinishingBuilder) this);
      iFileWatcherConfigurationBuilder = FileWatcherConfiguration.newBuilder().withParentBuilder(iFileWatchBuilder);
    }

    /**
     * Set the configured {@link FileWatcherConfiguration}
     * @return The {@link Builder} to continue
     */
    protected Builder withFileWatcherConfiguration(FileWatcherConfiguration aFileWatcherConfiguration)
    {
      iWatchConfiguration = aFileWatcherConfiguration;
      return this;
    }

    /**
     * Set the configured {@link IFileWatcher}
     * @return The {@link Builder} to continue
     */
    protected Builder withFileWatcher(IFileWatcher aFileWatcher)
    {
      iFileWatcher = aFileWatcher;
      return this;
    }

    /**
     * Finish the process by building the {@link IFileWatcher} and {@link FileWatcherConfiguration}
     * @return The build {@link IFileWatcher}
     */
    protected IFileWatcher finish()
    {
      iWatcherManager.getPathPairMap().put(iFileWatcher.getWatchedPath(),
        new Pair<>(iWatchConfiguration, iFileWatcher));

      return iFileWatcher;
    }

    @Override
    public FileWatcherConfiguration.Builder configure()
    {
      return iFileWatcherConfigurationBuilder;
    }

    @Override
    public IFileWatcher build()
    {
      return withFileWatcher(iFileWatchBuilder.build())
        .withFileWatcherConfiguration(iFileWatcherConfigurationBuilder.build()).finish();
    }
  }

  /**
   * Interface to hide build functionality
   */
  interface IFinishingBuilder
  {
    IFileWatcher build();
  }

  /**
   * Interface to reduce functionality to configure
   */
  interface IStartingBuilder
  {
    FileWatcherConfiguration.Builder configure();
  }
}
