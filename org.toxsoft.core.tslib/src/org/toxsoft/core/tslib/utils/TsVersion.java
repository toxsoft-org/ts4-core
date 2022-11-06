package org.toxsoft.core.tslib.utils;

import static org.toxsoft.core.tslib.utils.ITsResources.*;

import java.io.*;
import java.time.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * A generalization of the concept of a "version" of something.
 *
 * @author hazard157
 */
public final class TsVersion
    implements Serializable, Comparable<TsVersion> {

  private static final long serialVersionUID = 157157L;

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "TsVersion"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<TsVersion> KEEPER =
      new AbstractEntityKeeper<>( TsVersion.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, TsVersion aEntity ) {
          aSw.writeInt( aEntity.verMajor() );
          aSw.writeSeparatorChar();
          aSw.writeInt( aEntity.verMinor() );
          aSw.writeSeparatorChar();
          aSw.writeTimestamp( aEntity.verDate() );
        }

        @Override
        protected TsVersion doRead( IStrioReader aSr ) {
          int verMajor = aSr.readInt();
          aSr.ensureSeparatorChar();
          int verMinor = aSr.readInt();
          aSr.ensureSeparatorChar();
          long verDate = aSr.readTimestamp();
          return new TsVersion( verMajor, verMinor, verDate );
        }
      };

  private final short verMajor;
  private final short verMinor;
  private final long  verDate;

  // TODO TRANSLATE

  /**
   * Создать версию со всеми инвариантами.
   *
   * @param aMajor short - старший номер версии.
   * @param aMinor short - младший номер версии.
   * @param aVerDate long - метка времени версии
   * @throws TsIllegalArgumentRtException aMajor или aMinor < 0
   */
  public TsVersion( short aMajor, short aMinor, long aVerDate ) {
    TsIllegalArgumentRtException.checkTrue( aMajor < 0 || aMinor < 0 );
    verMajor = aMajor;
    verMinor = aMinor;
    verDate = aVerDate;
  }

  /**
   * Создать версию со всеми инвариантами.
   *
   * @param aMajor byte - старший номер версии.
   * @param aMinor byte - младший номер версии.
   * @param aVerDate long - метка времени (миллисекунды с начала эпохи 01.01.1970).
   */
  public TsVersion( int aMajor, int aMinor, long aVerDate ) {
    this( (short)aMajor, (short)aMinor, aVerDate );
  }

  /**
   * Constructor.
   *
   * @param aMajor int - major version
   * @param aMinor int minor version
   * @param aYear int - year
   * @param aMonth {@link Month} - month
   * @param aDayOfMonth int day of month (1..31)
   */
  public TsVersion( int aMajor, int aMinor, int aYear, Month aMonth, int aDayOfMonth ) {
    this( (short)aMajor, (short)aMinor, makeTimestamp( aYear, aMonth, aDayOfMonth ) );
  }

  static long makeTimestamp( int aYear, Month aMonth, int aDayOfMonth ) {
    LocalDate ld = LocalDate.of( aYear, aMonth, aDayOfMonth );
    ZonedDateTime zdt = ZonedDateTime.of( ld, LocalTime.NOON, ZoneOffset.UTC );
    Instant inst = zdt.toInstant();
    return inst.toEpochMilli();
  }

  /**
   * Создает версию с датой сборки - текущий момент и пустым описанием.
   *
   * @param aMajor short - старший номер версии.
   * @param aMinor short - младший номер версии.
   */
  public TsVersion( short aMajor, short aMinor ) {
    this( aMajor, aMinor, System.currentTimeMillis() );
  }

  /**
   * Создает версию с датой сборки - текущий момент и пустым описанием.
   *
   * @param aMajor int - старший номер версии.
   * @param aMinor int - младший номер версии.
   */
  public TsVersion( int aMajor, int aMinor ) {
    this( aMajor, aMinor, System.currentTimeMillis() );
  }

  /**
   * Конструктор копирования.
   *
   * @param aSource TsVersion - версия - источник
   */
  public TsVersion( TsVersion aSource ) {
    this( aSource.verMajor(), aSource.verMinor(), aSource.verDate() );
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  /**
   * Returns the version string in format "1.0. 2022-12-31 23:59:59".
   * <p>
   */
  @Override
  public String toString() {
    Long date = Long.valueOf( verDate() );
    Integer vMaj = Integer.valueOf( verMajor() );
    Integer vMin = Integer.valueOf( verMinor() );
    return String.format( "%d.%d %tF %tT", vMaj, vMin, date, date ); //$NON-NLS-1$
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + (int)(verDate ^ (verDate >>> 32));
    result = TsLibUtils.PRIME * result + verMajor;
    result = TsLibUtils.PRIME * result + verMinor;
    return result;
  }

  @Override
  public boolean equals( Object obj ) {
    if( this == obj ) {
      return true;
    }
    if( obj == null ) {
      return false;
    }
    if( getClass() != obj.getClass() ) {
      return false;
    }
    TsVersion other = (TsVersion)obj;
    if( verDate != other.verDate ) {
      return false;
    }
    if( verMajor != other.verMajor ) {
      return false;
    }
    if( verMinor != other.verMinor ) {
      return false;
    }
    return true;
  }

  // --------------------------------------------------------------------------
  // Реализация интерфейса Comparable<TsVersion>
  //

  @Override
  public int compareTo( TsVersion aThat ) {
    if( aThat == null ) {
      throw new NullPointerException();
    }
    if( aThat == this ) {
      return 0;
    }
    int c = Short.compare( this.verMinor, aThat.verMinor );
    if( c == 0 ) {
      c = Short.compare( this.verMajor, aThat.verMajor );
      if( c == 0 ) {
        c = Long.compare( this.verDate, aThat.verDate );
      }
    }
    return c;
  }

  // --------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает младший номер версии модуля.
   *
   * @return byte - неотрицательный младший номер
   */
  public short verMajor() {
    return verMajor;
  }

  /**
   * Возвращает младший номер версии модуля.
   *
   * @return byte - неотрицательный младший номер
   */
  public short verMinor() {
    return verMinor;
  }

  /**
   * Возвращает дату сборки версии модуля.
   *
   * @return long - метка времени в миллисекундах с начала эпохи
   */
  public long verDate() {
    return verDate;
  }

  /**
   * Creates and returns version number string.
   * <p>
   * Returned string format is part of the API. String has format like "1.3", that is formatted as <br>
   * <b><tt>Formatter.format("%d.%d", verMajor, verMinor);</tt> </b>
   *
   * @param aVersion {@link TsVersion} - the version
   * @return String - version string, like "1.3"
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static String getVersionNumber( TsVersion aVersion ) {
    TsNullArgumentRtException.checkNull( aVersion );
    Integer vMaj = Integer.valueOf( aVersion.verMajor() );
    Integer vMin = Integer.valueOf( aVersion.verMinor() );
    return String.format( "%d.%d", vMaj, vMin ); //$NON-NLS-1$
  }

  /**
   * Creates {@link TsVersion} instance from the string of format "1.0. 2022-12-31 23:59:59".
   *
   * @param aVerString String - formatted version string
   * @return {@link TsVersion} - creted instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException invalid format
   */
  public static TsVersion parseVersionString( String aVerString ) {
    TsNullArgumentRtException.checkNull( aVerString );
    IStrioReader sr = new StrioReader( new CharInputStreamString( aVerString ) );
    sr.setSkipMode( EStrioSkipMode.SKIP_NONE );
    int verMajor;
    int verMinor;
    int year;
    int month;
    int day;
    int hour;
    int minute;
    int second;
    try {
      verMajor = sr.readInt();
      sr.ensureChar( '.' );
      verMinor = sr.readInt();
      sr.ensureChar( ' ' );
      year = sr.readInt();
      sr.ensureChar( '-' );
      month = sr.readInt();
      sr.ensureChar( '-' );
      day = sr.readInt();
      sr.ensureChar( ' ' );
      hour = sr.readInt();
      sr.ensureChar( ':' );
      minute = sr.readInt();
      sr.ensureChar( ':' );
      second = sr.readInt();
    }
    catch( Exception e ) {
      throw new TsIllegalArgumentRtException( ERR_MSG_INV_VERSION_STRING_FORMAT, e );
    }
    LocalDateTime ldt = LocalDateTime.of( year, month, day, hour, minute, second );
    long verDate = ldt.atZone( ZoneId.systemDefault() ).toInstant().toEpochMilli();
    return new TsVersion( verMajor, verMinor, verDate );
  }

}
