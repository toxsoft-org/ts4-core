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

  private static final long serialVersionUID = 157157157L;

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

  /**
   * Constructor.
   *
   * @param aMajor short - version major number
   * @param aMinor short - version minor number
   * @param aVerDate long - version time stamp
   * @throws TsIllegalArgumentRtException aMajor or aMinor < 0
   */
  public TsVersion( short aMajor, short aMinor, long aVerDate ) {
    TsIllegalArgumentRtException.checkTrue( aMajor < 0 || aMinor < 0 );
    verMajor = aMajor;
    verMinor = aMinor;
    verDate = aVerDate;
  }

  /**
   * Constructor.
   *
   * @param aMajor int - version major number
   * @param aMinor int - version minor number
   * @param aVerDate long - version time stamp
   * @throws TsIllegalArgumentRtException aMajor or aMinor < 0
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
   * Constructor with current time stamp.
   *
   * @param aMajor short - version major number
   * @param aMinor short - version minor number
   */
  public TsVersion( short aMajor, short aMinor ) {
    this( aMajor, aMinor, System.currentTimeMillis() );
  }

  /**
   * Constructor with current time stamp.
   *
   * @param aMajor int - version major number
   * @param aMinor int - version minor number
   */
  public TsVersion( int aMajor, int aMinor ) {
    this( aMajor, aMinor, System.currentTimeMillis() );
  }

  /**
   * Copy constructor.
   *
   * @param aSource TsVersion - the source
   */
  public TsVersion( TsVersion aSource ) {
    this( aSource.verMajor(), aSource.verMinor(), aSource.verDate() );
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  /**
   * Returns the version string in format "1.0. 2022-12-31 23:59:59".
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
  // Comparable
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
   * Return the version major number.
   *
   * @return short - major number (always >=9)
   */
  public short verMajor() {
    return verMajor;
  }

  /**
   * Return the version major number.
   *
   * @return short - major number (always >=9)
   */
  public short verMinor() {
    return verMinor;
  }

  /**
   * Return version time stamp.
   *
   * @return long - version time stamp
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
   * Creates {@link TsVersion} instance from the string of format "1.0 2022-12-31 23:59:59".
   *
   * @param aVerString String - formatted version string
   * @return {@link TsVersion} - created instance
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
