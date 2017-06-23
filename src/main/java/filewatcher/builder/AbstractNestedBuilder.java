/*
 *
 *    Copyright (C) Marcel Meyer- All Rights Reserved
 *  *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  *  Proprietary and confidential
 *  *  Written by Marcel Meyer, devbeg@gmx.net, 2017
 *
 */

package filewatcher.builder;

import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Marcel on 18.06.2017.
 * The base class to extend when implementing a NestedBuilder. A NestedBuilder provides the function to set a iParent,
 * which is returned when calling done(). This can be used to design fluent apis in that way that you can control the
 * builder sequence when creating an object.
 */
public abstract class AbstractNestedBuilder<T, V>
{
  private final static Logger cLog = Logger.getLogger(AbstractNestedBuilder.class);

  private T iParent;

  /**
   * Method to call when all mandatory and optional properties were set. The method will return the specified iParent
   * class to continue building process.
   *
   * @return T The instance of the iParent builder
   */
  public T done()
  {
    Class<?> tParentClass = iParent.getClass();
    try
    {
      V tBuild = this.build();
      String tMethodName = "with" + tBuild.getClass().getSimpleName();
      Method tMethod = tParentClass.getDeclaredMethod(tMethodName, tBuild.getClass());
      tMethod.invoke(iParent, tBuild);
    }
    catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e)
    {
      cLog.error(e.getClass().getSimpleName() + " occured when trying to return iParent instance.", e);
    }
    return iParent;
  }

  /**
   * Build the class the AbstractNestedBuilder implementation is for
   * @return The class to build
   */
  protected abstract V build();

  /**
   * Set the parent builder to return to when calling done
   * @param parent The parent builder to set
   * @return The instance of the AbstractNestedBuilder implementation
   */
  public <P extends AbstractNestedBuilder<T, V>> P withParentBuilder(T parent)
  {
    this.iParent = parent;
    return (P) this;
  }
}
