/*
 *
 *    Copyright (C) Marcel Meyer- All Rights Reserved
 *  *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  *  Proprietary and confidential
 *  *  Written by Marcel Meyer, devbeg@gmx.net, 2017
 *
 */

package filewatcher.api;

import filewatcher.builder.MANAGER_RESPONSE;
import filewatcher.builder.FileWatcherConfiguration;
import filewatcher.builder.IFluentBuilder;
import javafx.util.Pair;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Marcel on 16.06.2017. Interface for FileWatcherManager which provides access to the
 * public functions of the FileWatcherManager.
 */
public interface IFileWatcherManager
{
  /**
   * Get the map containing all configured {@link IFileWatcher}s and
   * {@link FileWatcherConfiguration}s by their {@link Path}
   *
   * @return The {@link Pair <IFileWatcher, FileWatcherConfiguration>} by {@link Path} {@link Map}
   */
  Map<Path, Pair<FileWatcherConfiguration, IFileWatcher>> getPathPairMap();

  /**
   * Add an new {@link IFileWatcher} to manage by the {@link IFileWatcherManager}
   * 
   * @return The {@link IFluentBuilder.IStartingBuilder} to configure the {@link IFileWatcher} and
   * {@link FileWatcherConfiguration}. Please read documentation for {@link IFluentBuilder},
   * {@link FileWatcherConfiguration} and {@link IFileWatcher} carefully!
   */
  IFluentBuilder.IStartingBuilder addFileWatcher() throws IOException;

  /**
   * Call to check if a Path is watched
   * 
   * @param aPath The path to check for watching
   * @return MANAGER_RESPONSE which signals the state of the watcher
   */
  MANAGER_RESPONSE isWatching(Path aPath);

  /**
   * Call to stop watching path
   * 
   * @param aPath The path to stop watching
   * @return MANAGER_RESPONSE which signals the state of the watcher
   */
  MANAGER_RESPONSE stopWatching(Path aPath);

  /**
   * Call to start watching path
   *
   * @param aPath The path to start watching
   * @return MANAGER_RESPONSE which signals the state of the watcher
   */
  MANAGER_RESPONSE startWatching(Path aPath) throws IOException;

  /**
   * Stop all {@link filewatcher.api.IFileWatcher}s
   * 
   * @return MANAGER_RESPONSE which signals the state of the watcher
   */
  Map<Path, MANAGER_RESPONSE> stopAll();

  /**
   * Start all {@link filewatcher.api.IFileWatcher}s
   * 
   * @return MANAGER_RESPONSE which signals the state of the watcher
   */
  Map<Path, MANAGER_RESPONSE> startAll();

  /**
   * Delete the {@link IFileWatcher} for the {@link Path}
   * 
   * @return MANAGER_RESPONSE which signals the state of the watcher
   */
  MANAGER_RESPONSE deleteFileWatcher(Path aPath);

  /**
   * Get all yet configured watched {@link Path}s
   * 
   * @return All yet configured watched {@link Path}s
   */
  Collection<Path> getAllWatchedPaths();

  /**
   * Get {@link FileWatcherConfiguration} for a {@link Path}
   * 
   * @param aPath The {@link Path} to get the {@link FileWatcherConfiguration} for
   * @return An {@link Optional<FileWatcherConfiguration>}
   */
  Optional<FileWatcherConfiguration> getWatcherConfiguration(Path aPath);

  /**
   * Get {@link IFileWatcher} for a {@link Path}
   * 
   * @param aPath The {@link Path} to get the {@link IFileWatcher} for
   * @return An {@link Optional<IFileWatcher>}
   */
  Optional<IFileWatcher> getFileWatcher(Path aPath);
}
