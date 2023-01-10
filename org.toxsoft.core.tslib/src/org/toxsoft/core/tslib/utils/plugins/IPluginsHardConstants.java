package org.toxsoft.core.tslib.utils.plugins;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.utils.plugins.ITsResources.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Plugin manager constants.
 * <p>
 * Плагин представляет собой описание {@link IPluginInfo} и класс, который создается конструктором без аргументов, из
 * плагина. Один JAR-файл (именуемый контейнером плагинов) может содержать несколько плагинов.
 * <p>
 * Описание плагинов размещается в манифесте плагина META-INF/MANIFEST.MF. Требования к манифесту для того, чтобы он
 * описывал контейнер плагинов, следующие:
 * <ul>
 * <li>в главном разделе манифеста должно быть свойство {@link #MF_MAIN_ATTR_PLUGIN_CONTAINER_VERSION};</li>
 * <li>манифест манифест может содеражть луюбое количество любых разделов, однако, некоторые разделы интерпретируются
 * как описания плаг инов (один раздел на один плагин);</li>
 * <li>название раздела (т.е. первое свойство с именем "Name:") должно содержать идентификатор
 * {@link IPluginInfo#pluginId()} плагина;</li>
 * <li>раздел плагина обязан содержать следубщие свойства: {@link #MF_ATTR_PLUGIN_CLASS_NAME},
 * {@link #MF_ATTR_PLUGIN_TYPE} и {@link #MF_ATTR_PLUGIN_VERSION};</li>
 * <li>раздел плагина может содержать любое количество описаний зависимостей плагина. Описание зависимости это свойства
 * с именем, начинающемся с {@link #MF_ATTR_PREFIX_DEPENDENCY};</li>
 * <li>кроме того, раздел может содержать произвольное количество пользовательских свойств, которые возвращаются методом
 * {@link IPluginInfo#userProperties()}.</li>
 * </ul>
 *
 * @author hazard157
 */
public interface IPluginsHardConstants {

  // ------------------------------------------------------------------------------------
  // Plugin's MANIFEST.MF file constants
  //

  /**
   * Версия формата манифеста плагина.
   */
  int TS_PLUGIN_CONTAINER_MAINFEST_VERSION = 2;

  /**
   * Название атриабута главного раздела манифеста, который идентифицирует JAR-файл, как контейнер плагинов.
   */
  String MF_MAIN_ATTR_PLUGIN_CONTAINER_VERSION = "TsPluginContainerVersion"; //$NON-NLS-1$

  /**
   * Название свойства "версия плагина".
   * <p>
   * Значение свойства: {@link TsVersion#toString()}.
   */
  String MF_ATTR_PLUGIN_VERSION = "TsPluginVersion"; //$NON-NLS-1$

  /**
   * Название свойства "тип плагина".
   * <p>
   * Значение свойства: ИД-путь идентификатор типа плагина
   */
  String MF_ATTR_PLUGIN_TYPE = "TsPluginType"; //$NON-NLS-1$

  /**
   * Название свойства "имя класса плагина".
   * <p>
   * Значение свойства: полное название класса плагина {@link Class#getName()}. Класс плагина должен иметь открытый
   * конструктор без аргументов, для создания экземпляра методом {@link Class#newInstance()}.
   */
  String MF_ATTR_PLUGIN_CLASS_NAME = "TsPluginClassName"; //$NON-NLS-1$

  /**
   * Префикс названий свойств "требует другой плагин для работы".
   * <p>
   * Название свойства формируется добавлением к настоящему префиксу произвольной строки (естественно, не содержащей
   * двоеточия ':').
   * <p>
   * Значение свойства: это строка описания зависимости, имеющий вид:<br>
   * <code>тип_плагина ; идентификатор_плагина ; требуемая_версия_модуля ; нужна_ли_точная_версия </code> где:
   * <ul>
   * <li>; - разделитель {@link #MF_DEPENDENCY_LINE_PARTS_DELIMITER};</li>
   * <li>тип_плагина - строковый тип плагина</li>
   * <li>идентификатор_плагина - строковы идентификатор плагина;</li>
   * <li>требуемая_версия_модуля - строка (в кавычках) <b>"M.N 2011-12-31 23:59:59"</b> (формат строки соответствует
   * {@link TsVersion#toString()};</li>
   * <li>нужна_ли_точная_версия - признак того, что требуется точная версия (значение true) или указанная вресия и любая
   * более поздняя (false), ну.</li>
   * </ul>
   */
  String MF_ATTR_PREFIX_DEPENDENCY = "TsDependency."; //$NON-NLS-1$

  /**
   * Разделитель частей в строке описания зависимости.
   */
  String MF_DEPENDENCY_LINE_PARTS_DELIMITER = ";"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Runtime configuration options
  //

  /**
   * Тип плагинов, обрабатываемых компонентой.<br>
   * Тип данных: примитивный {@link EAtomicType#STRING}<br>
   * Формат: ИД-путь или пустая строка (обозначает все типы плагинов)<br>
   * Значение по умолчанию: "foo"
   */
  IDataDef PLUGIN_TYPE_ID = DataDef.create( "PluginManager.pluginTypeID", STRING, //$NON-NLS-1$
      TSID_NAME, E_N_PLOPS_PLUGIN_TYPE_ID, //
      TSID_DESCRIPTION, E_D_PLOPS_PLUGIN_TYPE_ID, //
      TSID_IS_MANDATORY, AV_TRUE, //
      TSID_DEFAULT_VALUE, avStr( "foo" ) //$NON-NLS-1$
  );

  /**
   * Интервал времени (мсек) между проверками директория на обновление плагинов.<br>
   * Тип данных: примитивный {@link EAtomicType#INTEGER}<br>
   * Формат: положительное целое числе, интервал вв миллисекундах, не менее 10000<br>
   * Значение по умолчанию: 300_000 (5 минут)
   */
  IDataDef DIR_CHECK_INTERVAL = DataDef.create( "PluginManager.dirCheckInterval", INTEGER, //$NON-NLS-1$
      TSID_NAME, E_N_PLOPS_DIR_CHECK_INTERVAL, //
      TSID_DESCRIPTION, E_D_PLOPS_DIR_CHECK_INTERVAL, //
      TSID_IS_MANDATORY, AV_FALSE, //
      TSID_DEFAULT_VALUE, avInt( 300_000 ) //
  );

}
