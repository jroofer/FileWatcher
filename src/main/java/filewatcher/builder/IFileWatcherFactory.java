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

import java.io.IOException;

/**
 * Created by Marcel on 18.06.2017.
 * IFileWatcherFactory provides methods to create a new Builder of different kind like {@link IFluentBuilder}
 */
public interface IFileWatcherFactory
{
  /**
   * Create new {@link IFluentBuilder}
   * @param aWatcherManager The {@link FileWatcherManager} to use
   * @return The {@link filewatcher.builder.IFluentBuilder.IStartingBuilder} to start building with
   * @throws IOException
   */
  static IFluentBuilder.IStartingBuilder newFluentBuilder(IFileWatcherManager aWatcherManager) throws IOException
  {
    IFluentBuilder.IStartingBuilder tBuilder = new IFluentBuilder.Builder(aWatcherManager);

    return tBuilder;
  }

}
