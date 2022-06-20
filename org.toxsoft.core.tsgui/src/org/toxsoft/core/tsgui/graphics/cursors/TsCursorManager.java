package org.toxsoft.core.tsgui.graphics.cursors;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Реализация {@link ITsCursorManager}
 *
 * @author vs
 */
public class TsCursorManager
    implements ITsCursorManager {

  private final IEclipseContext appContext;
  private final Display         display;

  final IStringMapEdit<Cursor> cursorsMap = new StringMap<>();

  /**
   * Конструктор.
   *
   * @param aAppContext {@link IEclipseContext} - контекст приложения
   * @throws TsNullArgumentRtException аргумент = null
   */
  public TsCursorManager( IEclipseContext aAppContext ) {
    TsNullArgumentRtException.checkNull( aAppContext );
    appContext = aAppContext;
    display = appContext.get( Display.class );
    TsItemNotFoundRtException.checkNull( display );
    // запланируем освобождение ресурсов
    display.disposeExec( () -> {
      while( !cursorsMap.isEmpty() ) {
        Cursor c = cursorsMap.removeByKey( cursorsMap.keys().first() );
        c.dispose();
      }
    } );
  }

  // ------------------------------------------------------------------------------------
  // ITsCursorManager
  //

  @Override
  public Cursor getCursor( ECursorType aCursorType ) {
    TsNullArgumentRtException.checkNull( aCursorType );
    return findCursor( aCursorType.id() );
  }

  @Override
  public Cursor findCursor( String aCursorId ) {
    TsNullArgumentRtException.checkNull( aCursorId );
    Cursor c = cursorsMap.findByKey( aCursorId );
    if( c != null ) {
      return c;
    }
    ECursorType cursorType = ECursorType.asList().findByKey( aCursorId );
    if( cursorType != null ) {
      c = new Cursor( display, cursorType.swtCursorCode() );
      cursorsMap.put( aCursorId, c );
    }
    return c;
  }

  @Override
  public boolean hasCursor( String aCursorId ) {
    return findCursor( aCursorId ) != null;
  }

  @Override
  public void putCursor( String aCursorId, Cursor aCursor ) {
    StridUtils.checkValidIdPath( aCursorId );
    TsNullArgumentRtException.checkNull( aCursor );
    TsIllegalArgumentRtException.checkTrue( aCursor.isDisposed() );
    Cursor c = cursorsMap.removeByKey( aCursorId );
    if( c != null ) {
      c.dispose();
    }
    cursorsMap.put( aCursorId, aCursor );
  }

}
