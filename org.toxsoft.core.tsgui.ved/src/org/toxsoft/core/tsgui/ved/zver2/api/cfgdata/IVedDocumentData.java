package org.toxsoft.core.tsgui.ved.zver2.api.cfgdata;

import org.toxsoft.core.tsgui.ved.zver2.api.*;
import org.toxsoft.core.tsgui.ved.zver2.api.comp.*;
import org.toxsoft.core.tsgui.ved.zver2.api.doc.*;
import org.toxsoft.core.tsgui.ved.zver2.incub.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * VED document data to be stored in database, file, etc.
 *
 * @author hazard157
 */
public interface IVedDocumentData {

  /**
   * Returns document properties values.
   *
   * @return {@link IOptionSet} - values for {@link IVedComponent#props()}
   */
  IOptionSet documentPropValues();

  /**
   * Returns config data to create components in document.
   *
   * @return {@link IStridablesList}&lt;{@link IVedEntityConfig}&gt; - data to create {@link IVedDocument#components()}
   */
  IStridablesList<IVedEntityConfig> componentConfigs();

  /**
   * Returns config data to create tailors in document.
   *
   * @return {@link IStridablesList}&lt;{@link IVedEntityConfig}&gt; - data to create {@link IVedDocument#tailors()}
   */
  IStridablesList<IVedEntityConfig> tailorConfigs();

  /**
   * Returns the bindings of each tailor from the list {@link #tailorConfigs()}.
   *
   * @return {@link IStringMap}&lt;{@link IStridablesList}&lt;{@link IVedBindingCfg}&gt;&gt; - map tailr ID - bind cfgs
   */
  IStringMap<IStridablesList<IVedBindingCfg>> tailorBindingConfigs();

  /**
   * Returns config data to create actors in document.
   *
   * @return {@link IStridablesList}&lt;{@link IVedEntityConfig}&gt; - data to create {@link IVedDocument#actors()}
   */
  IStridablesList<IVedEntityConfig> actorConfigs();

  /**
   * Returns application-specific data sections.
   * <p>
   * {@link IVedFramework} does not uses section data, it's up to the application to use it.
   *
   * @return {@link IKtorSectionsContainer} - data sections
   */
  IKtorSectionsContainer secitonsData();

}
