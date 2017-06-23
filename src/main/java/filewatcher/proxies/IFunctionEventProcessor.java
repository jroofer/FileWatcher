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
import java.util.function.Function;

/**
 * Created by Marcel on 23.06.2017.
 * IEventProcessor implementation to specify a Function to apply the WatchEvents on
 */
public class IFunctionEventProcessor<T> implements IEventProcessor
{
  private Function<WatchEvent<Path>, T> iFunction;

  /**
   * Default constructor for IFunctionEventProcessor
   * @param aWatchEventTFunction The Function to apply the WatchEvent<Path> on
   */
  public IFunctionEventProcessor(Function<WatchEvent<Path>, T> aWatchEventTFunction)
  {
    iFunction = aWatchEventTFunction;
  }

  @Override
  public void processEvents(Collection<WatchEvent<Path>> aWatchEvents)
  {
    aWatchEvents.forEach(tWatchEvent -> iFunction.apply(tWatchEvent));
  }
}
