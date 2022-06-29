package org.toxsoft.core.tsgui.ved.incub.lpd;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ILpdComponentInfo} editable implememtaion.
 *
 * @author hazard157
 */
public class LpdComponentInfo
    implements ILpdComponentInfo {

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<ILpdComponentInfo> KEEPER =
      new AbstractEntityKeeper<>( ILpdComponentInfo.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, ILpdComponentInfo aEntity ) {
          IdChain.KEEPER.write( aSw, aEntity.namespace() );
          aSw.writeSeparatorChar();
          aSw.writeAsIs( aEntity.componentKindId() );
          aSw.writeSeparatorChar();
          aSw.writeAsIs( aEntity.componentId() );
          aSw.writeSeparatorChar();
          OptionSetKeeper.KEEPER.write( aSw, aEntity.propValues() );
          aSw.writeSeparatorChar();
          OptionSetKeeper.KEEPER.write( aSw, aEntity.extdata() );
        }

        @Override
        protected ILpdComponentInfo doRead( IStrioReader aSr ) {
          IdChain namespace = IdChain.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          String kindId = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          String compId = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          IOptionSet props = OptionSetKeeper.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          IOptionSet extdata = OptionSetKeeper.KEEPER.read( aSr );
          return new LpdComponentInfo( namespace, kindId, compId, props, extdata );
        }
      };

  private final IdChain        namespace;
  private final String         kindId;
  private final String         compId;
  private final IOptionSetEdit props   = new OptionSet();
  private final IOptionSetEdit extdata = new OptionSet();

  /**
   * Constructor.
   *
   * @param aNamespace {@link IdChain} - the libarary ID (an IDpath)
   * @param aKindId String - the component kind ID (an IDpath)
   * @param aCompId String - the component ID (an IDpath)
   * @param aProps {@link IOptionSet} - {@link #propValues()} values
   * @param aExtdata {@link IOptionSet} - {@link #extdata()} values
   */
  public LpdComponentInfo( IdChain aNamespace, String aKindId, String aCompId, IOptionSet aProps,
      IOptionSet aExtdata ) {
    namespace = TsNullArgumentRtException.checkNull( aNamespace );
    kindId = StridUtils.checkValidIdPath( aKindId );
    compId = StridUtils.checkValidIdPath( aCompId );
    props.addAll( aProps );
    extdata.addAll( aExtdata );
  }

  @Override
  public IdChain namespace() {
    return namespace;
  }

  @Override
  public String componentKindId() {
    return kindId;
  }

  @Override
  public String componentId() {
    return compId;
  }

  @Override
  public IOptionSet propValues() {
    return props;
  }

  @Override
  public IOptionSet extdata() {
    return extdata;
  }

}
