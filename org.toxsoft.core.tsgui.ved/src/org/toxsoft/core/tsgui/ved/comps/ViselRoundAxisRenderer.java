package org.toxsoft.core.tsgui.ved.comps;

/**
 * Отрисовщик круговой шкалы.
 *
 * @author vs
 */
public class ViselRoundAxisRenderer {
  // extends AbstractViselRenderer {
  //
  // /**
  // * Arc renderer kind id
  // */
  // public static final String KIND_ID = "roundAxisRenderer"; //$NON-NLS-1$
  //
  // static final String PROPID_START_ANGLE = "startAngle"; //$NON-NLS-1$
  // static final String PROPID_DELTA_ANGLE = "deltaAngle"; //$NON-NLS-1$
  // static final String PROPID_BIG_TICK_QTTY = "bigTickQtty"; //$NON-NLS-1$
  // static final String PROPID_LIT_TICK_QTTY = "litTickQtty"; //$NON-NLS-1$
  // static final String PROPID_ANNOTATIONS = "annotations"; //$NON-NLS-1$
  // static final String PROPID_TEXT_ROTATION = "textRotation"; //$NON-NLS-1$
  // static final String PROPID_DRAW_ARC = "drawArc"; //$NON-NLS-1$
  // static final String PROPID_TICKS_OUT = "ticksOut"; //$NON-NLS-1$
  //
  // /**
  // * Start angle
  // */
  // public static final IDataDef PROP_START_ANGLE = DataDef.create3( PROPID_START_ANGLE, DT_FLOATING, //
  // TSID_NAME, STR_VISEL_ARC_START_ANGLE, //
  // TSID_DESCRIPTION, STR_VISEL_ARC_START_ANGLE_D, //
  // TSID_DEFAULT_VALUE, avFloat( 0 ) );
  //
  // /**
  // * End angle
  // */
  // public static final IDataDef PROP_DELTA_ANGLE = DataDef.create3( PROPID_DELTA_ANGLE, DT_FLOATING, //
  // TSID_NAME, STR_VISEL_ARC_DELTA_ANGLE, //
  // TSID_DESCRIPTION, STR_VISEL_ARC_DELTA_ANGLE_D, //
  // TSID_DEFAULT_VALUE, avFloat( 180 ) );
  //
  // static final ITinFieldInfo TFI_BIG_TICK_QTTY = TtiUtils.intFieldInfo( PROPID_BIG_TICK_QTTY, //
  // STR_ROUND_AXIS_BIG_TICK_QTTY, STR_ROUND_AXIS_BIG_TICK_QTTY_D );
  //
  // static final ITinFieldInfo TFI_LIT_TICK_QTTY = TtiUtils.intFieldInfo( PROPID_LIT_TICK_QTTY, //
  // STR_ROUND_AXIS_LIT_TICK_QTTY, STR_ROUND_AXIS_LIT_TICK_QTTY_D );
  //
  // static final ITinFieldInfo TFI_ANNOTATIONS = TtiUtils.strFieldInfo( PROPID_ANNOTATIONS, //
  // STR_ROUND_AXIS_ANNOTATIONS, STR_ROUND_AXIS_ANNOTATIONS_D );
  //
  // static final ITinFieldInfo TFI_TEXT_ROTATION = TtiUtils.booleanFieldInfo( PROPID_TEXT_ROTATION, //
  // STR_ROUND_AXIS_TEXT_ROTATION, STR_ROUND_AXIS_TEXT_ROTATION_D );
  //
  // static final ITinFieldInfo TFI_TICKS_OUT = TtiUtils.booleanFieldInfo( PROPID_TICKS_OUT, //
  // STR_ROUND_AXIS_TICKS_OUT, STR_ROUND_AXIS_TICKS_OUT_D );
  //
  // static final ITinFieldInfo TFI_DRAW_ARC = TtiUtils.booleanFieldInfo( PROPID_DRAW_ARC, //
  // STR_ROUND_AXIS_DRAW_ARC, STR_ROUND_AXIS_DRAW_ARC_D );
  //
  // static final IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
  //
  // static {
  // fields.add( TFI_RADIUS );
  // fields.add( new TinFieldInfo( PROPID_START_ANGLE, TTI_AT_FLOATING, PROP_START_ANGLE.params() ) );
  // fields.add( new TinFieldInfo( PROPID_DELTA_ANGLE, TTI_AT_FLOATING, PROP_DELTA_ANGLE.params() ) );
  // fields.add( TFI_ANNOTATIONS );
  // fields.add( TFI_BIG_TICK_QTTY );
  // fields.add( TFI_BIG_TICK_QTTY );
  // fields.add( TFI_DRAW_ARC );
  // fields.add( TFI_TEXT_ROTATION );
  // fields.add( TFI_TICKS_OUT );
  // fields.add( TFI_FG_COLOR );
  // fields.add( TFI_TEXT_COLOR );
  // fields.add( TFI_LINE_INFO );
  // }
  //
  // private double cx = 0;
  // private double cy = 0;
  // private Color color = new Color( 0, 0, 0 );
  // private Color textColor = new Color( 0, 0, 0 );
  // private double startAngle = 0;
  // private double deltaAngle = 90;
  // private double radius = 100;
  //
  // private int bigTickQtty = 2;
  //
  // private boolean drawArc = false;
  // private boolean ticksOut = true;
  // private boolean textRotation = true;
  //
  // private TsLineInfo lineInfo = TsLineInfo.DEFAULT;
  //
  // private IStringListEdit annotations = new StringArrayList();
  //
  // /**
  // * Constructor.
  // *
  // * @param aId String - idnetifier
  // * @param aPropDefs IStridablesList&lt;IDataDef> aPropDefs - props definitions
  // * @param aVisel {@link IVedVisel} - the corresponding VISEL
  // * @param aTsContext {@link ITsGuiContext} - corresponding context
  // */
  // public ViselRoundAxisRenderer( String aId, IStridablesList<IDataDef> aPropDefs, IVedVisel aVisel,
  // ITsGuiContext aTsContext ) {
  // super( aId, aPropDefs, aVisel, aTsContext );
  // }
  //
  // // ------------------------------------------------------------------------------------
  // // IViselRenderer
  // //
  //
  // @Override
  // public String kindId() {
  // return KIND_ID;
  // }
  //
  // @Override
  // protected ITinTypeInfo doCreateTypeInfo() {
  // // TODO Auto-generated method stub
  // return null;
  // }
  //
  // @Override
  // public IStridablesList<ITinFieldInfo> tinFieldInfoes() {
  // return fields;
  // }
  //
  // @Override
  // public void setPropValues( IOptionSet aProps ) {
  // if( aProps.hasKey( PROPID_X ) ) {
  // cx = aProps.getDouble( PROPID_X );
  // }
  // if( aProps.hasKey( PROPID_Y ) ) {
  // cy = aProps.getDouble( PROPID_Y );
  // }
  // if( aProps.hasKey( PROPID_FG_COLOR ) ) {
  // RGBA fgRgba = aProps.getByKey( PROPID_FG_COLOR ).asValobj();
  // color = colorManager().getColor( fgRgba );
  // }
  // if( aProps.hasKey( PROPID_TEXT_COLOR ) ) {
  // TsColorDescriptor cd = aProps.getByKey( PROPID_TEXT_COLOR ).asValobj();
  // textColor = colorManager().getColor( cd );
  // }
  // if( aProps.hasKey( PROPID_START_ANGLE ) ) {
  // startAngle = aProps.getByKey( PROPID_START_ANGLE ).asDouble() + 180;
  // }
  // if( aProps.hasKey( PROPID_DELTA_ANGLE ) ) {
  // deltaAngle = aProps.getByKey( PROPID_DELTA_ANGLE ).asDouble();
  // }
  // if( aProps.hasKey( TFI_RADIUS.id() ) ) {
  // radius = aProps.getDouble( TFI_RADIUS.id() );
  // }
  // if( aProps.hasKey( PROPID_LINE_INFO ) ) {
  // lineInfo = aProps.getByKey( PROPID_LINE_INFO ).asValobj();
  // }
  // if( aProps.hasKey( PROPID_RADIUS ) ) {
  // radius = aProps.getByKey( PROPID_RADIUS ).asDouble();
  // }
  // if( aProps.hasKey( TFI_BIG_TICK_QTTY.id() ) ) {
  // bigTickQtty = aProps.getByKey( TFI_BIG_TICK_QTTY.id() ).asInt();
  // }
  // if( aProps.hasKey( TFI_ANNOTATIONS.id() ) ) {
  // annotations.clear();
  // String annotationsStr = aProps.getByKey( TFI_ANNOTATIONS.id() ).asString();
  // StringTokenizer st = new StringTokenizer( annotationsStr, ";" ); //$NON-NLS-1$
  // while( st.hasMoreElements() ) {
  // annotations.add( st.nextToken().trim() );
  // }
  // }
  //
  // if( aProps.hasKey( PROPID_DRAW_ARC ) ) {
  // drawArc = aProps.getBool( PROPID_DRAW_ARC );
  // }
  // if( aProps.hasKey( PROPID_TEXT_ROTATION ) ) {
  // textRotation = aProps.getBool( PROPID_TEXT_ROTATION );
  // }
  // if( aProps.hasKey( PROPID_TICKS_OUT ) ) {
  // ticksOut = aProps.getBool( PROPID_TICKS_OUT );
  // }
  //
  // }
  //
  // @Override
  // public void doPaint( ITsGraphicsContext aPaintContext ) {
  // if( drawArc ) {
  // aPaintContext.setForegroundRgb( color.getRGB() );
  // aPaintContext.drawArc( (8), (8), (int)(2 * radius - 32), (int)(2 * radius - 32), //
  // (int)(startAngle - 180), (int)(startAngle + deltaAngle - 180) );
  // }
  //
  // double btDeltaAngle = (deltaAngle / (bigTickQtty - 1)) / 10;
  // for( int i = 0; i <= (bigTickQtty - 1) * 10; i++ ) {
  // double angle = startAngle + i * btDeltaAngle;
  // double sin = Math.sin( Math.toRadians( angle ) );
  // double cos = Math.cos( Math.toRadians( angle ) );
  // int l = 8;
  // if( i % 10 == 0 ) {
  // l = 16;
  // if( annotations.size() > i / 10 ) {
  // String text = annotations.get( i / 10 );
  // Point extent = aPaintContext.gc().textExtent( text );
  // double x = radius + (radius + extent.y / 2) * cos;
  // double y = radius + (radius + extent.y / 2) * sin;
  // drawRotatedLabel( aPaintContext.gc(), text, (int)(cx + x), (int)(cy + y), (float)angle + 90, extent );
  // }
  // }
  // double x1 = radius + (radius - 16) * cos;
  // double y1 = radius + (radius - 16) * sin;
  // double x2 = radius + (radius - 16 + l) * cos;
  // double y2 = radius + (radius - 16 + l) * sin;
  // aPaintContext.setForegroundRgb( color.getRGB() );
  // aPaintContext.drawLine( (int)(cx + x1), (int)(cy + y1), (int)(cx + x2), (int)(cy + y2) );
  // }
  // }
  //
  // // ------------------------------------------------------------------------------------
  // // Implementation
  // //
  //
  // private void drawRotatedLabel( GC gc, String aText, int aTcx, int aTcy, float aRotDeg, Point aExtent ) {
  // Display d = Display.getCurrent();
  // Transform oldTransform = new Transform( d );
  // gc.getTransform( oldTransform );
  //
  // // Размер текста для центрирования
  // int tw = aExtent.x;
  // int th = aExtent.y;
  //
  // Transform tr = new Transform( d );
  // gc.getTransform( tr );
  // // Переносим начало координат в точку подписи, поворачиваем, рисуем со смещением -tw/2, -th/2
  // tr.translate( aTcx, aTcy );
  // tr.rotate( aRotDeg );
  // gc.setTransform( tr );
  //
  // // gc.setForeground( color );
  // // drawText без фона (SWT.DRAW_TRANSPARENT)
  // gc.setForeground( textColor );
  // gc.drawText( aText, -tw / 2, -th / 2, true );
  //
  // // Восстанавливаем трансформацию
  // gc.setTransform( oldTransform );
  // oldTransform.dispose();
  // tr.dispose();
  // }

}
