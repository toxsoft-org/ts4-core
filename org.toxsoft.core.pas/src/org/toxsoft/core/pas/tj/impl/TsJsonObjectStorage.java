package org.toxsoft.core.pas.tj.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.pas.tj.ITjObject;
import org.toxsoft.core.pas.tj.ITjValue;
import org.toxsoft.core.tslib.bricks.strio.IStrioReader;
import org.toxsoft.core.tslib.bricks.strio.IStrioWriter;

/**
 * Загрузка/сохранение сущностей {@link ITjObject} в JSON представление.
 *
 * @author hazard157
 */
class TsJsonObjectStorage {

  public static TsJsonObjectStorage STORAGE = new TsJsonObjectStorage();

  TsJsonObjectStorage() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public ITjObject load( IStrioReader aSr ) {
    ITjObject obj = new TjObject();
    if( aSr.readArrayBegin() ) {
      do {
        String fieldName = aSr.readQuotedString();
        aSr.ensureChar( ':' );
        ITjValue fieldValue = TsJsonValueStorage.STORAGE.load( aSr );
        obj.fields().put( fieldName, fieldValue );
      } while( aSr.readArrayNext() );
    }
    return obj;
  }

  public void save( IStrioWriter aSw, ITjObject aObject ) {
    aSw.writeChar( CHAR_SET_BEGIN );
    // запись пустого объекта
    if( aObject.fields().isEmpty() ) {
      aSw.writeChar( CHAR_SET_END );
      return;
    }
    aSw.incNewLine();
    // записи "имя_поля": значение_поля,
    for( int i = 0, count = aObject.fields().size(); i < count; i++ ) {
      String fieldName = aObject.fields().keys().get( i );
      ITjValue fieldValue = aObject.fields().values().get( i );
      aSw.writeQuotedString( fieldName );
      aSw.writeChar( ':' );
      aSw.writeSpace();
      TsJsonValueStorage.STORAGE.save( aSw, fieldValue );
      if( i < count - 1 ) {
        aSw.writeSeparatorChar();
        aSw.writeEol();
      }
    }
    aSw.decNewLine();
    aSw.writeChar( CHAR_SET_END );
  }

}
