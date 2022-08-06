package org.toxsoft.core.tsgui.ved.tools_old.tools2.impl;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.ved.core.*;
import org.toxsoft.core.tsgui.ved.core.library.*;
import org.toxsoft.core.tsgui.ved.core.view.*;
import org.toxsoft.core.tsgui.ved.tools_old.tools2.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Base implementation of {@link IVedEditorToolProvider}.
 *
 * @author hazard157
 */
public abstract class VedAbstractEditorToolProvider
    extends StridableParameterized
    implements IVedEditorToolProvider, IStdParameterized {

  private final String libraryId;
  private final String groupId;

  /**
   * Constructor.
   *
   * @param aLibraryId Sting the owner library ID
   * @param aId String - provider ID (tool kind ID)
   * @param aGroupId String - tools group ID (an IDpath) or an empty string
   * @param aParams {@link IOptionSet} - {@link #params()} initial values
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any ID is not an IDpath
   */
  public VedAbstractEditorToolProvider( String aLibraryId, String aId, String aGroupId, IOptionSet aParams ) {
    super( aId, aParams );
    libraryId = StridUtils.checkValidIdPath( aLibraryId );
    TsNullArgumentRtException.checkNull( aGroupId );
    if( !aGroupId.isEmpty() ) {
      StridUtils.checkValidIdPath( aGroupId );
    }
    groupId = aGroupId;
  }

  /**
   * Constructor for tool outside of groups.
   *
   * @param aLibraryId Sting the owner library ID
   * @param aId String - provider ID (tool kind ID)
   * @param aParams {@link IOptionSet} - {@link #params()} initial values
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any ID is not an IDpath
   */
  public VedAbstractEditorToolProvider( String aLibraryId, String aId, IOptionSet aParams ) {
    this( aLibraryId, aId, TsLibUtils.EMPTY_STRING, aParams );
  }

  // ------------------------------------------------------------------------------------
  // IIconIdable
  //

  @Override
  public String iconId() {
    return params().getStr( TSID_ICON_ID, null );
  }

  // ------------------------------------------------------------------------------------
  // IVedEditorToolProvider
  //

  @Override
  final public String libraryId() {
    return libraryId;
  }

  @Override
  final public String groupId() {
    return groupId;
  }

  @Override
  final public IVedEditorTool createTool( IVedEnvironment aEnvironment, IVedScreen aScreen ) {
    TsNullArgumentRtException.checkNulls( aEnvironment, aScreen );
    // create tool
    Object rawTool = doCreateTool( aEnvironment, aScreen );
    if( rawTool instanceof VedAbstractEditorTool tool ) {
      TsInternalErrorRtException.checkNull( tool );
      TsInternalErrorRtException.checkTrue( tool.provider() != this );
      return tool;
    }
    throw new TsInternalErrorRtException();
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Implementation must create the tool.
   *
   * @param aEnvironment {@link IVedEnvironment} - the target environment
   * @param aScreen {@link IVedScreen} - owner screen
   * @return {@link VedAbstractEditorTool} - created tool
   */
  protected abstract VedAbstractEditorTool doCreateTool( IVedEnvironment aEnvironment, IVedScreen aScreen );

}
