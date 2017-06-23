/*
 *
 *    Copyright (C) Marcel Meyer- All Rights Reserved
 *  *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  *  Proprietary and confidential
 *  *  Written by Marcel Meyer, devbeg@gmx.net, 2017
 *
 */

package filewatcher.api;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.Collection;

/**
 * Created by Marcel on 16.06.2017.
 * Interface which provides a single method that is used to process {@link WatchEvent<Path>}
 */
@FunctionalInterface
public interface IEventProcessor
{
  /**
   * Function to be implemented for processing {@link WatchEvent<Path>}s
   * @param aWatchEvents The Collection of {@link WatchEvent<Path>}
   */
  void processEvents(Collection<WatchEvent<Path>> aWatchEvents);
}
