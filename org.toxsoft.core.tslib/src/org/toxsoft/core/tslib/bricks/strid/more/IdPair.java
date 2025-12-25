package org.toxsoft.core.tslib.bricks.strid.more;

import java.io.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * A paired identifier consisting of a left and a right ID-path identifier.
 * <p>
 * Парный идентификатор имеет текстовое представление {@link #pairId()}, которое <b>не является</b> ИД-путем, но
 * гарантированно не содержит в себе пробели.
 * <p>
 * Парный идентификатор {@link #pairId()} состоит из левого {@link #leftId()} и правого {@link #rightId()} ИД-путь
 * идентификаторов, соединенных методом {@link #makePairId(String, String)}. Отстувие левой или правой чати не
 * допускается.
 *
 * @author hazard157
 */
public final class IdPair
    implements Comparable<IdPair>, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * Идентификатор, обозначающий отсутствие вью или использование вью общего назначения.
   */
  public static final IdPair NONE = new IdPair( IStridable.NONE_ID, IStridable.NONE_ID );

  /**
   * Идентификатор регистрации хранителя.
   */
  public static final String KEEPER_ID = "IdPair"; //$NON-NLS-1$

  /**
   * Символ-разделитель между идентификаторами группы и вью.
   */
  public static final char CHAR_SEPARATOR = '$';

  /**
   * Разделитель для создания ИД-пути в методе {@link #asNonUniqueIdPath(IdPair)}.
   */
  private static final String IDPATH_SEPARATOR = "___"; //$NON-NLS-1$

  /**
   * Синглтон хранителя.
   */
  public static final IEntityKeeper<IdPair> KEEPER =
      new AbstractEntityKeeper<>( IdPair.class, EEncloseMode.NOT_IN_PARENTHESES, NONE ) {

        @Override
        protected void doWrite( IStrioWriter aSw, IdPair aEntity ) {
          aSw.writeAsIs( aEntity.pairId() );
        }

        @Override
        protected IdPair doRead( IStrioReader aSr ) {
          String leftId = aSr.readIdPath();
          EStrioSkipMode oldMode = aSr.skipMode();
          try {
            aSr.setSkipMode( EStrioSkipMode.SKIP_NONE );
            aSr.ensureChar( CHAR_SEPARATOR );
            String rightId = aSr.readIdPath();
            return new IdPair( leftId, rightId );
          }
          finally {
            aSr.setSkipMode( oldMode );
          }
        }
      };

  private transient String leftId  = null;
  private transient String rightId = null;
  private final String     pairId;

  /**
   * Создает экземпляр из левой и правой частей.
   *
   * @param aLeftId String - левый идентификатор
   * @param aRightId String - правый идентификатор
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   * @throws TsIllegalArgumentRtException любой идентификатор не ИД-путь
   */
  public IdPair( String aLeftId, String aRightId ) {
    pairId = makePairId( aLeftId, aRightId );
    leftId = aLeftId;
    rightId = aRightId;
  }

  /**
   * Создает экземпляр из текстового прдставления парного идентификатор.
   *
   * @param aPairId String - текст парного идентификатор
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   * @throws TsIllegalArgumentRtException неверный формат текстового представления
   */
  public IdPair( String aPairId ) {
    TsIllegalArgumentRtException.checkFalse( isValidPairId( aPairId ) );
    pairId = aPairId;
    initParts();
  }

  // метод выделен отдельно для восстановления после десерализаци
  private void initParts() {
    if( leftId == null || rightId == null ) {
      int index = pairId.indexOf( CHAR_SEPARATOR );
      leftId = pairId.substring( 0, index );
      rightId = pairId.substring( index + 1 );
    }
  }

  /**
   * Метод инициализирует transient поля и корректно восстанавливает сериализированный {@link #NONE}.
   *
   * @return Object объект {@link IAtomicValue#NULL}
   * @throws ObjectStreamException это обявление, оно тут не выбрасывается
   */
  private Object readResolve()
      throws ObjectStreamException {
    initParts();
    if( pairId.equals( NONE.pairId ) ) {
      return NONE;
    }
    return this;
  }

  // ------------------------------------------------------------------------------------
  // static API
  //

  /**
   * Создает текстовое представление парного идентификатора.
   *
   * @param aLeftId String - левый идентификатор
   * @param aRightId String - правый идентификатор
   * @return String - текст парного идентификатор
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   * @throws TsIllegalArgumentRtException любой идентификатор не ИД-путь
   */
  public static String makePairId( String aLeftId, String aRightId ) {
    StridUtils.checkValidIdPath( aLeftId );
    StridUtils.checkValidIdPath( aRightId );
    return aLeftId + CHAR_SEPARATOR + aRightId;
  }

  /**
   * Определяет, имеет ли текстовое представление парного идентификатора допустимый формат.
   *
   * @param aPairId String - текст парного идентификатор
   * @return boolean - признак правилного аргумента
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   * @throws TsIllegalArgumentRtException неверный формат текстового представления
   */
  public static boolean isValidPairId( String aPairId ) {
    TsNullArgumentRtException.checkNull( aPairId );
    int len = aPairId.length();
    if( len < 3 ) {
      return false;
    }
    int index = aPairId.indexOf( CHAR_SEPARATOR );
    if( index <= 0 || index >= len - 1 ) {
      return false;
    }
    String groupId = aPairId.substring( 0, index );
    String entryId = aPairId.substring( index + 1 );
    if( !StridUtils.isValidIdPath( groupId ) ) {
      return false;
    }
    if( !StridUtils.isValidIdPath( entryId ) ) {
      return false;
    }
    return true;
  }

  /**
   * Возвращает потенциально не уникальное представление в виде ИД-пути.
   *
   * @param aIdPair {@link IdPair} - парный идентификатор
   * @return String - ИД-путь
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static String asNonUniqueIdPath( IdPair aIdPair ) {
    TsNullArgumentRtException.checkNull( aIdPair );
    return aIdPair.leftId + IDPATH_SEPARATOR + aIdPair.rightId;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Определяет, явлется ли этот экземпляр константой {@link #NONE}.
   *
   * @return boolean - признак конатанты {@link #NONE}
   */
  public boolean isNone() {
    return pairId.equals( NONE.pairId );
  }

  /**
   * Возвращает левый идентификатор.
   *
   * @return String - левый идентификатор
   */
  public String leftId() {
    return leftId;
  }

  /**
   * Возвращает правый идентификатор.
   *
   * @return aRightId String - правый идентификатор
   */
  public String rightId() {
    return rightId;
  }

  /**
   * Возвращает текст парного идентификатора.
   *
   * @return String - текст парного идентификатора
   */
  public String pairId() {
    return pairId;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //

  @Override
  public String toString() {
    return pairId;
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof IdPair that ) {
      return pairId.equals( that.pairId );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + pairId.hashCode();
    return result;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса Comparable
  //

  @Override
  public int compareTo( IdPair aThat ) {
    if( aThat == null ) {
      throw new NullPointerException();
    }
    return pairId.compareTo( aThat.pairId() );
  }

}
