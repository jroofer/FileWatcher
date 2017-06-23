/*
 *
 *    Copyright (C) Marcel Meyer- All Rights Reserved
 *  *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  *  Proprietary and confidential
 *  *  Written by Marcel Meyer, devbeg@gmx.net, 2017
 *
 */

package filewatcher.builder;

/**
 * Created by Marcel on 16.06.2017.
 * Responses which are returned when calling function on FileWatcherManager
 */
public enum MANAGER_RESPONSE
{
  WATCHING,
  NOT_WATCHING,
  NO_WATCHER_FOUND,
  ALREADY_WATCHING,
  VALID,
  NOT_VALID,
  MISSING_FILE_WATCHER,
  NOT_REMOVED,
  REMOVED
}
