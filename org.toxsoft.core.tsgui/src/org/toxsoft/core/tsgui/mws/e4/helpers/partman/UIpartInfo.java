package org.toxsoft.core.tsgui.mws.e4.helpers.partman;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.icons.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * UIpart properties to manage them using {@link ITsPartStackManager}.
 *
 * @author hazard157
 */
public final class UIpartInfo {

  private final String partId;

  private String  label           = EMPTY_STRING;
  private String  tooltip         = EMPTY_STRING;
  private String  iconUri         = null;
  private String  contributionUri = null;
  private boolean closeable       = false;

  /**
   * Constructor.
   *
   * @param aPartId String - part ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is a blank string
   */
  public UIpartInfo( String aPartId ) {
    partId = TsErrorUtils.checkNonBlank( aPartId );
  }

  /**
   * Creates {@link UIpartInfo} filled with properties of argument.
   *
   * @param aPart {@link MPart} - an E4 UI part
   * @return {@link UIpartInfo} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static UIpartInfo ofPart( MPart aPart ) {
    TsNullArgumentRtException.checkNull( aPart );
    UIpartInfo pinf = new UIpartInfo( aPart.getElementId() );
    pinf.setCloseable( aPart.isCloseable() );
    pinf.setContributionUri( aPart.getContributionURI() );
    pinf.setIconUri( aPart.getIconURI() );
    pinf.setLabel( aPart.getLabel() );
    pinf.setTooltip( aPart.getTooltip() );
    return pinf;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * returns the part ID used as {@link MPart#getElementId()}.
   *
   * @return String - the part ID
   */
  public String partId() {
    return partId;
  }

  /**
   * Returns part label used as tab name {@link MPart#getLabel()}.
   *
   * @return String - visible label of the part
   */
  public String getLabel() {
    return label;
  }

  /**
   * Returns part label used as tab name {@link MPart#getLabel()}.
   *
   * @param aLabel String - visible label of the part
   */
  public void setLabel( String aLabel ) {
    label = aLabel;
  }

  /**
   * Returns the tooltip text.
   *
   * @return String - the tooltip text, may be empty but not <code>null</code>
   */
  public String getTooltip() {
    return tooltip;
  }

  /**
   * Sets the tooltip text.
   *
   * @param aTooltip String - the tooltip text
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setTooltip( String aTooltip ) {
    TsNullArgumentRtException.checkNull( aTooltip );
    tooltip = aTooltip;
  }

  /**
   * Returns the UIpart tab icon URI.
   *
   * @return String - the icon URI or null
   */
  public String getIconUri() {
    return iconUri;
  }

  /**
   * Sets the UIpart tab icon.
   *
   * @param aIconUri String - the icon URI string or <code>null</code>
   */
  public void setIconUri( String aIconUri ) {
    iconUri = aIconUri;
  }

  /**
   * Sets the UIpart tab icon.
   *
   * @param aPluginId String - icon provider plugin
   * @param aIconId String - the icon ID
   * @param aIconSize {@link EIconSize} - the icon size
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setIconUri( String aPluginId, String aIconId, EIconSize aIconSize ) {
    iconUri = TsIconManagerUtils.makeStdIconUriString( aPluginId, aIconId, aIconSize );
  }

  /**
   * Returns the URI of the class making part's content.
   *
   * @return String - contribution class URI
   */
  public String getContributionUri() {
    return contributionUri;
  }

  /**
   * Sets the URI of the class making part's content.
   * <p>
   * Contribution URI may be build as<br>
   * <code>String uri ="bundleclass://" + PLUGIN_ID + "/" + UipartClass.class.getName();</code>
   *
   * @param aContributionUri String - contribution class URI
   */
  public void setContributionUri( String aContributionUri ) {
    contributionUri = aContributionUri;
  }

  /**
   * Sets the URI of the class making part's content.
   *
   * @param aPluginId String - class provider plugin ID
   * @param aClass {@link Class} - the content class
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the plugin ID is a black string
   */
  public void setContributionUri( String aPluginId, Class<?> aClass ) {
    TsErrorUtils.checkNonBlank( aPluginId );
    TsNullArgumentRtException.checkNull( aClass );
    contributionUri = "bundleclass://" + aPluginId + '/' + aClass.getName(); //$NON-NLS-1$
  }

  /**
   * Returns the closable flag.
   *
   * @return boolean - the closable flag
   */
  public boolean isCloseable() {
    return closeable;
  }

  /**
   * Sets the closable flag.
   *
   * @param aCloseable boolean - the closable flag
   */
  public void setCloseable( boolean aCloseable ) {
    closeable = aCloseable;
  }

}
