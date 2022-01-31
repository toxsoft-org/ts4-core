package org.toxsoft.core.unit.txtproj.lib.tdfile;

import java.io.*;

import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.ICharInputStreamCloseable;
import org.toxsoft.core.tslib.bricks.strio.chario.ICharOutputStream;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.CharInputStreamFile;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.CharOutputStreamWriter;
import org.toxsoft.core.tslib.bricks.strio.impl.StrioReader;
import org.toxsoft.core.tslib.bricks.strio.impl.StrioWriter;
import org.toxsoft.core.tslib.utils.errors.TsIoRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Вспомогательные методы работы с {@link ITdFile}.
 *
 * @author hazard157
 */
public class TdFileUtils {

  /**
   * Считывает содержимое {@link ITdFile} из дискового файла.
   * <p>
   * Существующее в aTdf содержимое удаляется.
   *
   * @param aFile {@link File} - файл на диске
   * @param aTdf {@link ITdFile} - куда считать
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIoRtException файл недоступен для чтения
   * @throws StrioRtException неверный формат файла
   */
  public static void readTdFile( File aFile, ITdFile aTdf ) {
    TsNullArgumentRtException.checkNull( aTdf );
    try( ICharInputStreamCloseable chIn = new CharInputStreamFile( aFile ) ) {
      IStrioReader sr = new StrioReader( chIn );
      aTdf.read( sr );
    }
  }

  /**
   * Записывает содержимое {@link ITdFile} в дисковый файл.
   *
   * @param aFile {@link File} - файл на диске
   * @param aTdf {@link ITdFile} - что писать
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIoRtException ошибка записи
   */
  public static void writeTdFile( File aFile, ITdFile aTdf ) {
    TsNullArgumentRtException.checkNull( aTdf );
    try( FileWriter fw = new FileWriter( aFile ) ) {
      ICharOutputStream chOut = new CharOutputStreamWriter( fw );
      IStrioWriter dw = new StrioWriter( chOut );
      aTdf.write( dw );
    }
    catch( IOException ex ) {
      throw new TsIoRtException( ex );
    }
  }

  /**
   * Запрет на создание экземпляров.
   */
  private TdFileUtils() {
    // nop
  }

}
