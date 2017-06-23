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
 * Created by Marcel on 23.06.2017.
 * Function interface to implement for create a class which can sent notification in any way
 */
@FunctionalInterface
public interface Notifier
{
  <T extends Notification> boolean sent(T aNotification);
}
