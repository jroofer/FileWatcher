/*
 *
 *    Copyright (C) Marcel Meyer- All Rights Reserved
 *  *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  *  Proprietary and confidential
 *  *  Written by Marcel Meyer, devbeg@gmx.net, 2017
 *
 */

package filewatcher.proxies;

import filewatcher.api.IEventProcessor;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.Collection;

/**
 * Created by Marcel on 22.06.2017.
 */
public class DefaultEventProcessor implements IEventProcessor
{
  private final INotifiable iNotifiable;

  /**
   * Default constructor for DefaultEventProcessor
   * @param aINotifiable The INotifiable to notify on event
   */
  public DefaultEventProcessor(INotifiable aINotifiable)
  {
    iNotifiable = aINotifiable;
  }

  @Override public void processEvents(final Collection<WatchEvent<Path>> aWatchEvents)
  {
    aWatchEvents.forEach(tWatchEvent -> {
      iNotifiable.notifyMe(tWatchEvent.context(), tWatchEvent.kind());
    });
  }

  /**
   * Interface for objects which want to be notifiable
   */
  public static interface INotifiable
  {
    /**
     * Method to call to notify the observer
     * @param aPath The watched Path which has an event
     * @param aKind The Kind of event which occur
     */
    void notifyMe(Path aPath, WatchEvent.Kind aKind);
  }
}
