package org.toxsoft.core.tsgui.ved.comps.render;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.errors.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Base interface of factories of visel renderers.
 *
 * @author vs
 */
public interface IViselRendererFactory
    extends IStridableParameterized {

  /**
   * Returns the kind of the renderer, for example: "button", "RoundAxis" etc...
   *
   * @return {@link String} - the renderer kind
   */
  String kindId();

  /**
   * Returns the type information for item to be viewed and edited in object inspector.
   *
   * @return {@link ITinTypeInfo} - the information for inspecting the renderer instance
   */
  ITinTypeInfo typeInfo();

  /**
   * Returns the information about renderer properties.
   * <p>
   * The list of properties is unambiguously compiled from the information provided by {@link #typeInfo()}.
   *
   * @return {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   */
  IStridablesList<IDataDef> propDefs();

  /**
   * Возвращает конфигурацию отрисовщика с параматреми по-умолчанию.
   *
   * @param aId String - ИД конфигурации
   * @param aViselId String - ИД VISEL'я
   * @return ViselRendererCfg - конфигурация отрисовщика
   */
  ViselRendererCfg createConfig( String aId, String aViselId );

  /**
   * Creates the entity instance with default values of fields.
   *
   * @param aCfg {@link ViselRendererCfg} - the configuration data
   * @param aVisel {@link IVedVisel} - the corresponding visel
   * @param aVedScreen {@link VedScreen} - the owner screen
   * @return &lt;T&gt; - created instance of the item
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException config entity kind does not matches provided entity kind
   * @throws AvTypeCastRtException any property value is not compatible to the property definition
   */
  AbstractViselRenderer create( ViselRendererCfg aCfg, IVedVisel aVisel, VedScreen aVedScreen );

  /**
   * Возвращает текстовое представление конфигурации в виде короткой текстовой строки для пользователя.<br>
   * По-умолчанию, возвращает имя фабрики. Конкретная фабрика может переопределить этот метод. Например, для шкалы могут
   * возвращаться её пределы и т.п.
   *
   * @param aCfg {@link ViselRendererCfg} - конфигурация отрисвщика.
   * @return String - текстовое представление конфигурации в виде короткой текстовой строки для пользователя
   */
  default String getConfigTextRepresentation( ViselRendererCfg aCfg ) {
    return nmName();
  }
}
