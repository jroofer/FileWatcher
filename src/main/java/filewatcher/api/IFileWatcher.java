/*
 *
 *    Copyright (C) Marcel Meyer- All Rights Reserved
 *  *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  *  Proprietary and confidential
 *  *  Written by Marcel Meyer, devbeg@gmx.net, 2017
 *
 */

package filewatcher.api;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * Created by Marcel on 16.06.2017.
 * Interface for file watcher implementations which provide the functions which can be used
 */
public interface IFileWatcher
{
  static <T> WatchEvent<T> castEvent(WatchEvent<?> aWatchEvent)
  {
    return (WatchEvent<T>) aWatchEvent;
  }

  /**
   * Get the path watched by this IFileWatcher
   */
  Path getWatchedPath();

  /**
   * Get the flag indicating if the watcher runs infinite which means it restarts watching after event
   */
  boolean isInfinite();

  /**
   * Get the flag indicating if the IFileWatcher is running
   */
  boolean isRunning();

  /**
   * Get the flag indicating if the IFileWatcher is watching recursively
   * @return The flag
   */
  boolean isRecursiv();

  /**
   * Set the flag indicating if the IFileWatcher should watch recursively which includes all subdirectories.
   * NOTE: THIS CAN ONLY BE SET ON TRUE FOR DIRECTORIES AND WILL CAUSE EXCEPTION ON FILES!
   * @param aRecursiv The flag to set
   */
  void setRecursiv(boolean aRecursiv);

  /**
   * Set the {@link java.nio.file.WatchEvent.Kind}s to watch for
   * @param aKinds The {@link java.nio.file.WatchEvent.Kind}s to set
   */
  void setKinds(final WatchEvent.Kind[] aKinds);

  /**
   * Set infinite flag value {@see IFileWatcher#isInfinite()}
   * @param aInfinite The flag to set
   */
  void setInfinite(boolean aInfinite);

  /**
   * Add an {@link IEventProcessor} to process {@link WatchEvent}s
   * @param aProcessor The implementation of {@link IEventProcessor}
   */
  void addProcessor(final IEventProcessor aProcessor);

  /**
   * Start watching on path
   */
  void start() throws IOException;

  /**
   * Stop watching on path
   */
  void stop();
}
