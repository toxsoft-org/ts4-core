package org.toxsoft.core.tsgui.bricks.combicond;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.bricks.combicond.ICombiCondParamsPanelConstants.*;
import static org.toxsoft.core.tsgui.bricks.combicond.ITsResources.*;
import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.ITsHardConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.math.lexan.ILexanConstants.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.bricks.combicond.valed.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.generic.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.panels.misc.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.idgen.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.math.combicond.*;
import org.toxsoft.core.tslib.math.combicond.impl.*;
import org.toxsoft.core.tslib.math.lexan.*;
import org.toxsoft.core.tslib.math.lexan.impl.*;
import org.toxsoft.core.tslib.math.logican.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ICombiCondParamsPanel} implementation.
 * <p>
 * Panel contains:
 * <ul>
 * <li>formula string editor - SWT {@link Text} control to edit logical formula;</li>
 * <li>conditions list - to edit conditions mentioned in the formula by the keywords;</li>
 * <li>optional validation pane - {@link ValidationResultPanel} displaying the formula correctness.</li>
 * </ul>
 * <p>
 * Respects options from {@link ICombiCondParamsPanelConstants} and <code>aViewer</code> argument specified in the
 * constructor {@link CombiCondParamsPanel#CombiCondParamsPanel(ITsGuiContext, boolean)}.
 *
 * @author hazard157
 */
public class CombiCondParamsPanel
    extends AbstractLazyPanel<Control>
    implements IGenericEntityEditPanel<ICombiCondParams> {

  /**
   * Context option: The RGB color of the keyword text background in formula.
   */
  public static final IDataDef OPDEF_KEYWORD_BKG_RGB =
      DataDef.create( TS_FULL_ID + ".CombiCondParamsPanel.KeywordBkgRgb", VALOBJ, //$NON-NLS-1$
          TSID_NAME, STR_KEYWORD_BKG_RGB, //
          TSID_DESCRIPTION, STR_KEYWORD_BKG_RGB_D, //
          TSID_KEEPER_ID, RGBKeeper.KEEPER_ID, //
          TSID_DEFAULT_VALUE, avValobj( new RGB( 220, 220, 255 ) ) //
      );

  /**
   * Context option: The RGB color of the keyword text font in formula.
   */
  public static final IDataDef OPDEF_KEYWORD_FG_RGB =
      DataDef.create( TS_FULL_ID + ".CombiCondParamsPanel.KeywordBkgFg", VALOBJ, //$NON-NLS-1$
          TSID_NAME, STR_KEYWORD_FG_RGB, //
          TSID_DESCRIPTION, STR_KEYWORD_FG_RGB_D, //
          TSID_KEEPER_ID, RGBKeeper.KEEPER_ID, //
          TSID_DEFAULT_VALUE, avValobj( ETsColor.BLACK.rgb() ) //
      );

  /**
   * Context option: Flags that the keyword text will be displayed by bold font.
   */
  public static final IDataDef OPDEF_KEYWORD_IS_BOLD =
      DataDef.create( TS_FULL_ID + ".CombiCondParamsPanel.KeywordIsBold", BOOLEAN, //$NON-NLS-1$
          TSID_NAME, STR_KEYWORD_IS_BOLD, //
          TSID_DESCRIPTION, STR_KEYWORD_IS_BOLD_D, //
          TSID_KEEPER_ID, RGBKeeper.KEEPER_ID, //
          TSID_DEFAULT_VALUE, AV_TRUE //
      );

  /**
   * {@link MultiPaneComponent} implementation for SCPs list editing.
   *
   * @author hazard157
   */
  class FsiMpc
      extends MultiPaneComponentModown<FormulaScpItem> {

    public FsiMpc( ITsGuiContext aContext, IM5Model<FormulaScpItem> aModel,
        IM5ItemsProvider<FormulaScpItem> aItemsProvider, IM5LifecycleManager<FormulaScpItem> aLifecycleManager ) {
      super( aContext, aModel, aItemsProvider, aLifecycleManager );
      aspLocal.actionsStateEventer().addListener( s -> updateActionsState() );
    }

    @Override
    protected ITsToolbar doCreateToolbar( ITsGuiContext aContext, String aName, EIconSize aIconSize,
        IListEdit<ITsActionDef> aActs ) {
      aActs.add( ACDEF_SEPARATOR );
      aActs.addAll( aspLocal.listAllActionDefs() );
      return super.doCreateToolbar( aContext, aName, aIconSize, aActs );
    }

    @Override
    protected void doProcessAction( String aActionId ) {
      aspLocal.handleAction( aActionId );
    }

    @Override
    protected void doUpdateActionsState( boolean aIsAlive, boolean aIsSel, FormulaScpItem aSel ) {
      for( String aid : aspLocal.listHandledActionIds() ) {
        toolbar().setActionEnabled( aid, aspLocal.isActionEnabled( aid ) );
        toolbar().setActionChecked( aid, aspLocal.isActionChecked( aid ) );
      }
      updateValidationPane();
    }

  }

  /**
   * Local actions added to the standard actions of the toolbar.
   *
   * @author hazard157
   */
  class AspLocal
      extends MethodPerActionTsActionSetProvider {

    private static final String ACTID_ADD_ABSENT_KEYWORDS = "act.AddAbsentKeywords"; //$NON-NLS-1$
    private static final String ACTID_REMOVE_STALE_FSI    = "act.RemoveStaleFsi";    //$NON-NLS-1$

    private static final ITsActionDef AVDEF_ADD_ABSENT_KEYWORDS = TsActionDef.ofPush2( ACTID_ADD_ABSENT_KEYWORDS, //
        STR_ADD_ABSENT_KEYWORDS, STR_ADD_ABSENT_KEYWORDS_D, //
        ICONID_LIST_ADD //
    // ICONID_ADD_ABSENT_KEYWORDS //
    );

    private static final ITsActionDef AVDEF_REMOVE_STALE_FSI = TsActionDef.ofPush2( ACTID_REMOVE_STALE_FSI, //
        STR_REMOVE_STALE_FSI, STR_REMOVE_STALE_FSI_D, //
        ICONID_LIST_REMOVE //
    // ICONID_REMOVE_STALE_FSI //
    );

    public AspLocal() {
      defineAction( AVDEF_ADD_ABSENT_KEYWORDS, CombiCondParamsPanel.this::whenAddAbsentKeywords,
          CombiCondParamsPanel.this::isAbsentKeywords );
      defineAction( AVDEF_REMOVE_STALE_FSI, CombiCondParamsPanel.this::whenRemoveNotNeededFsi,
          CombiCondParamsPanel.this::isNotNeededFsi );
    }

  }

  private static final IStridGenerator fsiM5ModelIdGenerator = new UuidStridGenerator();

  private final AspLocal             aspLocal = new AspLocal();
  private final GenericChangeEventer genericChangeEventer;
  private final boolean              isViewer;

  private ISingleCondParams scpOfEmptyFormula = ISingleCondParams.NEVER;

  private final ISingleCondTypesRegistry<ISingleCondType> scpTypesReg = new DefaultSingleCondTypesRegistry();

  private final LogicalFormulaParser parser = new LogicalFormulaParser();

  private final IListEdit<FormulaScpItem> items = new ElemArrayList<>();

  private IFormulaTextWidget                       textWidget     = null;
  private ValidationResultPanel                    validationPane = null;
  private IM5CollectionPanel<FormulaScpItem>       scpPanel       = null;
  private MultiPaneComponentModown<FormulaScpItem> scpMpc         = null;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aViewer boolean - the flag to create the viewer, not an editor
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public CombiCondParamsPanel( ITsGuiContext aContext, boolean aViewer ) {
    super( aContext );
    genericChangeEventer = new GenericChangeEventer( this );
    isViewer = aViewer;
  }

  // ------------------------------------------------------------------------------------
  // AbstractLazyPanel
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    Composite board = new Composite( aParent, SWT.NONE );
    board.setLayout( new BorderLayout() );
    //
    boolean formulaAtTop = OPDEF_IS_FORMULA_EDITOR_AT_TOP.getValue( tsContext().params() ).asBool();
    boolean isValidationPane = OPDEF_IS_VALIDATION_PANE.getValue( tsContext().params() ).asBool();
    //
    if( isValidationPane ) {
      boolean vrPaneAtTop = OPDEF_IS_VALIDATION_PANE_AT_TOP.getValue( tsContext().params() ).asBool();
      // if both formula and validation panes are at same side, we'll need special board
      ITsGuiContext ctxVp = new TsGuiContext( tsContext() );
      // ValidationResultPanel.OPDEF_ICON_SIZE.setValue( ctxVp.params(), avValobj( EIconSize.IS_64X64 ) );
      if( formulaAtTop == vrPaneAtTop ) {
        Composite formulaBoard = new Composite( board, SWT.NONE ); // board for formula text and validation pane
        formulaBoard.setLayoutData( formulaAtTop ? BorderLayout.NORTH : BorderLayout.SOUTH );
        formulaBoard.setLayout( new BorderLayout() );
        validationPane = new ValidationResultPanel( formulaBoard, ctxVp );
        validationPane.setLayoutData( BorderLayout.CENTER );
        textWidget = new FormulaTextWidget( formulaBoard, tsContext() );
        textWidget.getControl().setLayoutData( BorderLayout.SOUTH );
      }
      else {
        validationPane = new ValidationResultPanel( board, ctxVp );
        validationPane.setLayoutData( vrPaneAtTop ? BorderLayout.NORTH : BorderLayout.SOUTH );
        textWidget = new FormulaTextWidget( board, tsContext() );
        textWidget.getControl().setLayoutData( formulaAtTop ? BorderLayout.NORTH : BorderLayout.SOUTH );
      }
    }
    else { // no validation pane
      textWidget = new FormulaTextWidget( board, tsContext() );
      textWidget.getControl().setLayoutData( formulaAtTop ? BorderLayout.NORTH : BorderLayout.SOUTH );
    }
    // scpList
    String modelId = fsiM5ModelIdGenerator.nextId();
    FormulaScpItemM5Model model =
        (FormulaScpItemM5Model)m5().initTemporaryModel( new FormulaScpItemM5Model( modelId, scpTypesReg ) );
    IM5LifecycleManager<FormulaScpItem> lm = model.getLifecycleManager( this );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    OPDEF_IS_ACTIONS_CRUD.setValue( ctx.params(), AV_TRUE );
    ValedSingleCondParams.REFDEF_SCT_REGISTRY.setRef( ctx, scpTypesReg );
    scpMpc = new FsiMpc( ctx, model, lm.itemsProvider(), lm );
    scpPanel = new M5CollectionPanelMpcModownWrapper<>( scpMpc, false );
    scpPanel.createControl( board );
    scpPanel.getControl().setLayoutData( BorderLayout.CENTER );
    // setup
    textWidget.genericChangeEventer().addListener( s -> whenFormulaTextEdited() );
    scpPanel.genericChangeEventer().addListener( s -> whenScpPanelChange() );
    return board;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void whenFormulaTextEdited() {
    updateFormulaHighlights();
    scpMpc.updateActionsState();
    genericChangeEventer.fireChangeEvent();
  }

  private void whenScpPanelChange() {
    parser.parse( textWidget.getFormulaText() );
    scpMpc.updateActionsState();
    genericChangeEventer.fireChangeEvent();
  }

  void whenAddAbsentKeywords() {
    IStringList absentKeywords = listAbsentKeywords();
    if( absentKeywords.isEmpty() ) {
      return;
    }
    for( String kw : absentKeywords ) {
      FormulaScpItem fsi = new FormulaScpItem( kw, ISingleCondParams.ALWAYS );
      items.add( fsi );
    }
    scpPanel.refresh();
    genericChangeEventer.fireChangeEvent();
  }

  boolean isAbsentKeywords() {
    return !listAbsentKeywords().isEmpty();
  }

  void whenRemoveNotNeededFsi() {
    IStringList formulaKeywords = parser.formulaTokens().listKeywords();
    for( int i = 0; i < items.size(); ) {
      String fsiKw = items.get( i ).keyword();
      if( !formulaKeywords.hasElem( fsiKw ) ) {
        items.removeByIndex( i );
      }
      else {
        ++i;
      }
    }
    scpPanel.refresh();
    genericChangeEventer.fireChangeEvent();
  }

  boolean isNotNeededFsi() {
    IStringList formulaKeywords = parser.formulaTokens().listKeywords();
    for( FormulaScpItem fsi : items ) {
      if( !formulaKeywords.hasElem( fsi.keyword() ) ) {
        return true;
      }
    }
    return false;
  }

  private void updateFormulaHighlights() {
    String text = textWidget.getFormulaText();
    ILogFoNode lfn = parser.parse( text );
    String tooltip;
    int errPos;
    IList<StyleRange> ll;
    if( parser.formulaTokens().isError() ) { // formula has errors
      errPos = LexanUtils.getErrorCharPos( parser.formulaTokens() );
      ILexanToken tk = parser.formulaTokens().firstErrorToken();
      tooltip = tk != null ? tk.str() : EMPTY_STRING;
      ll = IList.EMPTY;
    }
    else { // no errors in formula
      errPos = -1;
      tooltip = lfn.toString();
      ll = makeFormulaHighlights();
    }
    textWidget.setHighlights( errPos, ll );
    textWidget.setToolTipText( tooltip );
  }

  private void updateValidationPane() {
    ValidationResult vr = canGetEntity();
    if( validationPane != null ) {
      validationPane.setShownValidationResult( vr );
    }
  }

  private IList<StyleRange> makeFormulaHighlights() {
    IListEdit<StyleRange> ll = new ElemArrayList<>();
    RGB fgRgb = OPDEF_KEYWORD_FG_RGB.getValue( tsContext().params() ).asValobj();
    RGB bkRgb = OPDEF_KEYWORD_BKG_RGB.getValue( tsContext().params() ).asValobj();
    Color fgColor = colorManager().getColor( fgRgb );
    Color bkColor = colorManager().getColor( bkRgb );
    int fontStyle = SWT.NONE;
    if( OPDEF_KEYWORD_IS_BOLD.getValue( tsContext().params() ).asBool() ) {
      fontStyle = SWT.BOLD;
    }
    int starCharIndex = 0;
    for( int i = 0; i < parser.formulaTokens().tokens().size(); i++ ) {
      ILexanToken tk = parser.formulaTokens().tokens().get( i );
      int subsLen = parser.formulaTokens().subStrings().get( i ).length();
      if( tk.isKind( TKID_KEYWORD ) ) {
        if( subsLen > 0 ) {
          StyleRange r = new StyleRange( starCharIndex, subsLen, fgColor, bkColor, fontStyle );
          ll.add( r );
        }
      }
      starCharIndex += subsLen;
    }
    return ll;
  }

  private IStringMap<ISingleCondParams> makeScpMapFromFsiItems() {
    IStringMapEdit<ISingleCondParams> map = new StringMap<>();
    for( FormulaScpItem fsi : items ) {
      map.put( fsi.keyword(), fsi.scp() );
    }
    return map;
  }

  private IStringList listAbsentKeywords() {
    IStringList formulaKeywords = parser.formulaTokens().listKeywords();
    IStringListEdit absentKeywords = new StringArrayList( formulaKeywords );
    for( FormulaScpItem fsi : items ) {
      if( formulaKeywords.hasElem( fsi.keyword() ) ) {
        absentKeywords.remove( fsi.keyword() );
      }
    }
    return absentKeywords;
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public GenericChangeEventer genericChangeEventer() {
    return genericChangeEventer;
  }

  // ------------------------------------------------------------------------------------
  // IGenericContentPanel
  //

  @Override
  public boolean isViewer() {
    return isViewer;
  }

  // ------------------------------------------------------------------------------------
  // IGenericEntityPanel
  //

  @Override
  public void setEntity( ICombiCondParams aEntity ) {
    String formulaString = EMPTY_STRING;
    items.clear();
    if( aEntity != null ) {
      CombiCondParamsTokenizer cct = new CombiCondParamsTokenizer( aEntity );
      formulaString = cct.getFormulaString();
      // fill items for scpMpc
      for( String kw : cct.singleParams().keys() ) {
        FormulaScpItem fsi = new FormulaScpItem( kw, cct.singleParams().getByKey( kw ) );
        items.add( fsi );
      }
    }
    textWidget.setFormulaText( formulaString );
    updateFormulaHighlights();
    // scpMpc.updateActionsState();
    scpMpc.refresh();
  }

  // ------------------------------------------------------------------------------------
  // IGenericEntityEditPanel
  //

  @Override
  public ICombiCondParams getEntity() {
    TsValidationFailedRtException.checkError( canGetEntity() );
    if( parser.formulaTokens().isEmpty() ) {
      return CombiCondParams.createSingle( scpOfEmptyFormula );
    }
    return CombiCondParamsBuilder.INSTANCE.build( parser.getFormulaNode(), makeScpMapFromFsiItems() );
  }

  @Override
  public ValidationResult canGetEntity() {
    parser.parse( textWidget.getFormulaText() );
    // ERROR: error in formula
    if( parser.formulaTokens().isError() ) {
      String errMsg = parser.formulaTokens().firstErrorToken().str();
      return ValidationResult.error( errMsg );
    }
    // ERROR or SUCCESS: empty formula
    if( parser.formulaTokens().isEmpty() ) {
      if( scpOfEmptyFormula == null ) {
        return ValidationResult.error( MSG_ERR_EMPTY_FORMULA );
      }
      return ValidationResult.SUCCESS;
    }
    // ERROR: not all formula keywords has entries in #scpPanel
    IStringMap<ISingleCondParams> scpMap = makeScpMapFromFsiItems();
    IStringList formulaKeywords = parser.formulaTokens().listKeywords();
    for( String kw : formulaKeywords ) {
      if( !scpMap.hasKey( kw ) ) {
        return ValidationResult.error( FMT_ERR_FORMULA_KW_NOT_SCP, kw );
      }
    }
    ValidationResult vr = ValidationResult.SUCCESS;
    // WARN: if any SCP ALWAYS or NEVER is found in FSI items
    for( FormulaScpItem fsi : items ) {
      if( fsi.scp() == ISingleCondParams.ALWAYS || fsi.scp() == ISingleCondParams.NEVER ) {
        vr = ValidationResult.warn( MSG_WARN_SOME_SCP_ARE_FIXED );
        break;
      }
    }
    // WARN: unused entry in #scpPanel
    for( FormulaScpItem fsi : items ) {
      if( !formulaKeywords.hasElem( fsi.keyword() ) ) {
        vr = ValidationResult.firstNonOk( vr, ValidationResult.warn( FMT_WARN_UNUSED_SCP, fsi.keyword() ) );
        break;
      }
    }
    return ValidationResult.firstNonOk( vr,
        CombiCondParamsBuilder.INSTANCE.canBuild( parser.getFormulaNode(), scpMap ) );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Determines how the empty formula ({@link IFormulaTokens#isEmpty()} = <code>true</code>) is represented.
   * <p>
   * <code>null</code> means that empty formula is not allowed, {@link #getEntity()} with throw
   * {@link TsValidationFailedRtException} exception.
   * <p>
   * Common values are {@link ISingleCondParams#ALWAYS} or {@link ISingleCondParams#NEVER}. Default value is
   * {@link ISingleCondParams#NEVER}.
   *
   * @return {@link ISingleCondParams} - empty formula representation or <code>null</code>
   */
  public ISingleCondParams getEmptyFormulaRepresentation() {
    return scpOfEmptyFormula;
  }

  /**
   * Sets the value {@link #getEmptyFormulaRepresentation()}.
   *
   * @param aScp {@link ISingleCondParams} - empty formula representation or <code>null</code>
   */
  public void setEmptyFormulaRepresentation( ISingleCondParams aScp ) {
    scpOfEmptyFormula = aScp;
  }

  /**
   * Adds condition types from the argument to the internal registry.
   *
   * @param aTypes {@link IStridablesList}&lt;{@link ISingleCondType}&gt; - types to register
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException any type ID is already registered
   */
  public void addSctToUsedRegistry( IStridablesList<? extends ISingleCondType> aTypes ) {
    TsNullArgumentRtException.checkNull( aTypes );
    TsItemAlreadyExistsRtException
        .checkTrue( TsCollectionsUtils.intersects( aTypes.ids(), scpTypesReg.items().ids() ) );
    for( ISingleCondType t : aTypes ) {
      scpTypesReg.register( t );
    }
  }

  /**
   * Adds condition types from the argument to the internal registry.
   *
   * @param aTypesRegistry {@link IStridablesRegisrty} - the registry of {@link ISingleCondType}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException any type ID is already registered
   */
  public void addSctRegistry( IStridablesRegisrty<? extends ISingleCondType> aTypesRegistry ) {
    TsNullArgumentRtException.checkNull( aTypesRegistry );
    addSctToUsedRegistry( aTypesRegistry.items() );
  }

  // ------------------------------------------------------------------------------------
  // package API
  //

  LogicalFormulaParser parser() {
    return parser;
  }

  IListEdit<FormulaScpItem> fsiItems() {
    return items;
  }

}
