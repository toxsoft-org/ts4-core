package org.toxsoft.core.tslib.av.avtree;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Интерфейс редатирования описания дерева (структуры) значений {@link IAvTreeInfo}.
 * 
 * @author hazard157
 */
public interface IAvTreeInfoEdit
    extends IAvTreeInfo {

  /**
   * Добавляет описание поля в список {@link #fieldInfoes()}.
   * 
   * @param aFieldInfo {@link IAvTreeFieldInfo} - добавляемое описание поля
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemAlreadyExistsRtException описание с идентификатором аргумента уже существует
   */
  void addFieldInfo( IAvTreeFieldInfo aFieldInfo );

  /**
   * Удаляет описание поля из списка {@link #fieldInfoes()}.
   * <p>
   * Если описания с таким идентификатором нет в списке, то ничего не делает.
   * 
   * @param aFieldId String - идентификатор удаляемого описания поля
   */
  void removeFieldInfo( String aFieldId );

  /**
   * Очищает список описаний полей {@link #fieldInfoes()}.
   */
  void clearFields();

  /**
   * Добавляет описание в описания узлов дерева или в члены семейства.
   * <p>
   * В зависимости от того, редактируется ли описание семества ({@link #isFamily()}=true) или структуры дерева (
   * {@link #isFamily()}=false), данный метод работает соответственно с описанием членов семейства
   * {@link #familyMembers()} или описаниями узлов дерева {@link #nodeInfoes()}.
   * 
   * @param aAvTreeInfo {@link IAvTreeInfo} - добавляемое описание узла (члена семейства)
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemAlreadyExistsRtException описание с идентификатором аргумента уже существует
   */
  void addAvTreeInfo( IAvTreeInfo aAvTreeInfo );

  /**
   * Удаляет описание из описания узлов дерева или из членов семейства.
   * <p>
   * Если описания с таким идентификатором нет в списке, то ничего не делает.
   * <p>
   * В зависимости от того, редактируется ли описание семества ({@link #isFamily()}=true) или структуры дерева (
   * {@link #isFamily()}=false), данный метод работает соответственно с описанием членов семейства
   * {@link #familyMembers()} или описаниями узлов дерева {@link #nodeInfoes()}.
   * 
   * @param aAvTreeId String - идентификатор удаляемого описания
   * @throws TsNullArgumentRtException аргумент = null
   */
  void removeAvTreeInfo( String aAvTreeId );

  /**
   * Очищает список описаний узлов или членов семейства.
   * <p>
   * В зависимости от того, редактируется ли описание семества ({@link #isFamily()}=true) или структуры дерева (
   * {@link #isFamily()}=false), данный метод работает соответственно с описанием членов семейства
   * {@link #familyMembers()} или описаниями узлов дерева {@link #nodeInfoes()}.
   */
  void clearAvTrees();

}
