package org.toxsoft.core.txtproj.lib.tdfile;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.*;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Раздел файла {@link ITdFile}.
 * <p>
 * Сообщение {@link IGenericChangeListener#onGenericChangeEvent(Object)} генерируется при изменении содержимого
 * {@link #getContent()} в методе записи {@link #writeEntity(IKeepableEntity)}.
 *
 * @author hazard157
 */
public class TdfSection {

  private final GenericChangeEventer eventer;

  private final String keyword;

  /**
   * Содержимое раздела из "keyword = { ... }" (включая скобки)".
   */
  private String content = TsLibUtils.EMPTY_STRING;

  /**
   * Конструктор при создании пустого раздела.
   *
   * @param aKeyword String - ключевое слово (ИД-путь)
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException aKeyword не ИД-путь
   */
  public TdfSection( String aKeyword ) {
    this( aKeyword, TsLibUtils.EMPTY_STRING );
  }

  /**
   * Конструктор при создании раздела чтением из файла.
   *
   * @param aKeyword String - ключевое слово (ИД-путь)
   * @param aContent String - содержимое раздела между скобками
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aKeyword не ИД-путь
   */
  public TdfSection( String aKeyword, String aContent ) {
    eventer = new GenericChangeEventer( this );
    keyword = StridUtils.checkValidIdPath( aKeyword );
    content = TsNullArgumentRtException.checkNull( aContent );
  }

  // ------------------------------------------------------------------------------------
  // API класса
  //

  /**
   * Возващает ключевое слово, под которым записан раздел.
   *
   * @return String - ключевое слово (ИД-путь)
   */
  final public String keyword() {
    return keyword;
  }

  /**
   * Возвращает содержимое раздела в текстовом виде.
   * <p>
   * Содержимым раздела считается текст обрамленный скобками после ключевого слова раздела. Скобки включаются в
   * содержимое.
   *
   * @return String - содержимое раздела в текстовом виде
   */
  public String getContent() {
    return content;
  }

  /**
   * Считывает сущность.
   *
   * @param aEntity {@link IKeepableEntity} - сущность, в который считывается содержимое раздела
   * @throws TsNullArgumentRtException аргумент = null
   * @throws StrioRtException нарушение формата ханения или попытка прочитать не ту сущность
   */
  public void readEntity( IKeepableEntity aEntity ) {
    TsNullArgumentRtException.checkNull( aEntity );
    ICharInputStream chIn = new CharInputStreamString( getContent() );
    IStrioReader dr = new StrioReader( chIn );
    aEntity.read( dr );
  }

  /**
   * Записывает сущность в раздел.
   *
   * @param aEntity {@link IKeepableEntity} - записываемая сущность
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void writeEntity( IKeepableEntity aEntity ) {
    TsNullArgumentRtException.checkNull( aEntity );
    StringBuilder sb = new StringBuilder();
    ICharOutputStream chOut = new CharOutputStreamAppendable( sb );
    IStrioWriter dv = new StrioWriter( chOut );
    aEntity.write( dv );
    String newContent = sb.toString();
    if( !newContent.equals( content ) ) {
      content = newContent;
      eventer.fireChangeEvent();
    }
  }

  /**
   * Returns the change eventer.
   *
   * @return {@link IGenericChangeEventer} - the change eventer
   */
  public IGenericChangeEventer eventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методоы Object
  //

  @SuppressWarnings( "nls" )
  @Override
  public String toString() {
    return this.getClass().getSimpleName() + " - " + keyword;
  }

}
