package org.toxsoft.core.tslib.av.avtree;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Описание дерева переменных.
 * <p>
 * Этот интерфейс реализует {@link IStridable}, поля которого имеют следующий смысл:
 * <ul>
 * <li><b>id</b>() - уникальный (среди других узлов родительского дерева) идентификатор узла (ИД-путь);</li>
 * <li><b>description</b>() - удобочитаемое описание узла (используется например, как всплывающая подсказка, Tooltip,
 * пользователю при показе/редактировании содержимого узла).</li>
 * <li><b>name</b>() - удобочитаемое, краткое имя узла (используется например, как подпись (Label) к виджету на панели
 * при показе/редактировании содержимого узла).</li>
 * </ul>
 *
 * @author goga
 */
public interface IAvTreeInfo
    extends IStridable {

  /**
   * Определяет, будет ли значения описанного дерева единичным, или массивом.
   *
   * @return boolean - признак того, что узел значении является массивом<br>
   *         <b>true</b> - соответствующий {@link IAvTree#isArray()} = <code>true</code>;<br>
   *         <b>false</b> - соответствующий {@link IAvTree#isArray()} = <code>false</code>.
   */
  boolean isArray();

  /**
   * Возвращает признак того, что данный объект содержит описание семейства, а не отдельной структуры дерева.
   *
   * @return boolean - признак того, что данный объект содержит описание семейства, а не отдельной структуры дерева<br>
   *         <b>true</b> - описание содержит семейство, члены которого перечислены в {@link #familyMembers()};<br>
   *         <b>false</b> - описание содержит структуру дерева, возвращаемое методами {@link #fieldInfoes()} и
   *         {@link #nodeInfoes()}.
   */
  boolean isFamily();

  /**
   * Возвращает описания полей дерева (имеет смысл только при {@link #isFamily()}=false).
   * <p>
   * Эти описания соответствуют значениям полей, возвращаемые методом {@link IAvTree#fields()}.
   *
   * @return IStridablesList&lt;{@link IAvTreeFieldInfo}&gt; - список описания полей
   * @throws TsUnsupportedFeatureRtException это описание семейства, а не дерева (то есть {@link #isFamily()}=true)
   */
  IStridablesList<IAvTreeFieldInfo> fieldInfoes();

  /**
   * Возвращает описания дочерных узлов дерева (имеет смысл только при {@link #isFamily()}=false).
   * <p>
   * Эти описания соответствуют значениям узлов, возвращаемые методом {@link IAvTree#nodes()}.
   *
   * @return IStridablesList&lt;{@link IAvTreeInfo}&gt; - список описания дочерных подеревьев
   * @throws TsUnsupportedFeatureRtException это описание семейства, а не дерева (то есть {@link #isFamily()}=true)
   */
  IStridablesList<IAvTreeInfo> nodeInfoes();

  /**
   * Возвращает описания членов семейства (имеет смысл только при {@link #isFamily()}=true).
   *
   * @return IStridablesList&lt;{@link IAvTreeInfo}&gt; - перечень членов семейства
   * @throws TsUnsupportedFeatureRtException это описание дерева, а не семейства (то есть {@link #isFamily()}=false)
   */
  IStridablesList<IAvTreeInfo> familyMembers();

}
