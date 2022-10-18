package org.toxsoft.core.tsgui.ved.api.cfgdata;

import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.comp.*;
import org.toxsoft.core.tsgui.ved.api.doc.*;
import org.toxsoft.core.tsgui.ved.incub.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

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
   * @return {@link IStridablesList}&lt;{@link IVedComponentCfg}&gt; - data to create {@link IVedDocument#components()}
   */
  IStridablesList<IVedComponentCfg> componentConfigs();

  /**
   * Returns config data to create tailors in document.
   *
   * @return {@link IStridablesList}&lt;{@link IVedTailorCfg}&gt; - data to create {@link IVedDocument#tailors()}
   */
  IStridablesList<IVedTailorCfg> tailorConfigs();

  /**
   * Returns config data to create actors in document.
   *
   * @return {@link IStridablesList}&lt;{@link IVedActorCfg}&gt; - data to create {@link IVedDocument#actors()}
   */
  IStridablesList<IVedActorCfg> actorConfigs();

  /**
   * Returns application-specific data sections.
   * <p>
   * {@link IVedFramework} does not uses section data, it's up to the application to use it.
   *
   * @return {@link IKtorSectionsContainer} - data sections
   */
  IKtorSectionsContainer secitonsData();

}
