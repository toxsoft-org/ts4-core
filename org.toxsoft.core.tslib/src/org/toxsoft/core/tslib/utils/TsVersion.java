package org.toxsoft.core.tslib.utils;

import java.io.*;
import java.time.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.valobj.*;

// TODO TRANSLATE

/**
 * Обобщение понятия "версии" чего-либо.
 *
 * @author hazard157
 */
public final class TsVersion
    implements Serializable, Comparable<TsVersion> {

  private static final long serialVersionUID = 157157L;

  /**
   * Идентификатор регистрации хранителя {@link #KEEPER} в реестре {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "TsVersion"; //$NON-NLS-1$

  /**
   * Экземпляр-синглтон хранителя.
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
  // Переопределеные методы Object
  // Логика работы методов сравнения и вычисления хеш-кода должны соотвктствовать логике
  // работы метода VersionUtils.getVersionString()
  //

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

}
