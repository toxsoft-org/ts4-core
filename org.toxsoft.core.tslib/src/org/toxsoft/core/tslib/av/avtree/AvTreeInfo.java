package org.toxsoft.core.tslib.av.avtree;

import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Редактируемая реализация описание дерева (структуры) значений {@link IAvTreeInfo}.
 *
 * @author hazard157
 */
public class AvTreeInfo
    extends Stridable
    implements IAvTreeInfoEdit {

  private final boolean                               isFamilyTree;
  private final boolean                               isArray;
  private final IStridablesListEdit<IAvTreeFieldInfo> fieldInfoes   = new StridablesList<>();
  private final IStridablesListEdit<IAvTreeInfo>      nodeInfoes    = new StridablesList<>();
  private final IStridablesListEdit<IAvTreeInfo>      familyMembers = new StridablesList<>();

  /**
   * Создает описание фиксированной структуры (не семейства).
   *
   * @param aId String - идентификатор структуры дерева (ИД-путь)
   * @param aDescription String - удобочитаемое описание
   * @param aName String - краткое, удобочитаемое название
   * @param aFieldInfoes IStridablesList&lt;{@link IAvTreeFieldInfo}&gt; - описания полей структур
   * @param aNodeInfoes IStridablesList&lt;{@link IAvTreeInfo}&gt; - описания дочерных структур
   * @param aIsArray boolean - признак дерева-массива (а не единичного)
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aId не ИД-путь
   */
  public AvTreeInfo( String aId, String aDescription, String aName, IStridablesList<IAvTreeFieldInfo> aFieldInfoes,
      IStridablesList<IAvTreeInfo> aNodeInfoes, boolean aIsArray ) {
    super( aId, aDescription, aName, true );
    isFamilyTree = false;
    fieldInfoes.addAll( aFieldInfoes );
    nodeInfoes.addAll( aNodeInfoes );
    isArray = aIsArray;
  }

  /**
   * Создает описание семейства структур.
   *
   * @param aId String - идентификатор структуры дерева (ИД-путь)
   * @param aDescription String - удобочитаемое описание
   * @param aName String - краткое, удобочитаемое название
   * @param aFamilyMembers IStridablesList&lt;{@link IAvTreeInfo}&gt; - члены семейства
   * @param aIsArray boolean - признак дерева-массива (а не единичного)
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aId не ИД-путь
   * @throws TsItemNotFoundRtException нет члена семейства с идентификатором aSelectedMemberId
   */
  public AvTreeInfo( String aId, String aDescription, String aName, IStridablesList<IAvTreeInfo> aFamilyMembers,
      boolean aIsArray ) {
    super( aId, aDescription, aName, true );
    isFamilyTree = true;
    familyMembers.addAll( aFamilyMembers );
    isArray = aIsArray;
  }

  /**
   * Конструктор копирования.
   *
   * @param aSource {@link IAvTreeInfo} - исходный объект
   * @throws TsNullArgumentRtException аргумент = null
   */
  public AvTreeInfo( IAvTreeInfo aSource ) {
    super( TsNullArgumentRtException.checkNull( aSource ).id(), aSource.description(), aSource.nmName(), true );
    isFamilyTree = aSource.isFamily();
    isArray = aSource.isArray();
    if( isFamilyTree ) {
      familyMembers.addAll( aSource.familyMembers() );
    }
    else {
      fieldInfoes.addAll( aSource.fieldInfoes() );
      nodeInfoes.addAll( aSource.nodeInfoes() );
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса AvTreeInfo
  //

  @Override
  public boolean isArray() {
    return isArray;
  }

  @Override
  public IStridablesList<IAvTreeFieldInfo> fieldInfoes() {
    return fieldInfoes;
  }

  @Override
  public IStridablesList<IAvTreeInfo> nodeInfoes() {
    return nodeInfoes;
  }

  @Override
  public boolean isFamily() {
    return isFamilyTree;
  }

  @Override
  public IStridablesList<IAvTreeInfo> familyMembers() {
    return familyMembers;
  }

  // ------------------------------------------------------------------------------------
  // IAvTreeInfoEdit
  //

  @Override
  public void addFieldInfo( IAvTreeFieldInfo aFieldInfo ) {
    fieldInfoes.add( aFieldInfo );
  }

  @Override
  public void removeFieldInfo( String aFieldId ) {
    fieldInfoes.removeByKey( aFieldId );
  }

  @Override
  public void clearFields() {
    fieldInfoes.clear();
  }

  @Override
  public void addAvTreeInfo( IAvTreeInfo aAvTreeInfo ) {
    if( isFamilyTree ) {
      familyMembers.add( aAvTreeInfo );
    }
    else {
      nodeInfoes.add( aAvTreeInfo );
    }
  }

  @Override
  public void removeAvTreeInfo( String aAvTreeId ) {
    if( isFamilyTree ) {
      familyMembers.removeById( aAvTreeId );
    }
    else {
      nodeInfoes.removeById( aAvTreeId );
    }
  }

  @Override
  public void clearAvTrees() {
    if( isFamilyTree ) {
      familyMembers.clear();
    }
    else {
      nodeInfoes.clear();
    }
  }

}
