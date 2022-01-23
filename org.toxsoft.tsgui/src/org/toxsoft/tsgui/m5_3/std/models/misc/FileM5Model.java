package org.toxsoft.tsgui.m5_3.std.models.misc;

import static org.toxsoft.tsgui.m5_3.IM5Constants.*;
import static org.toxsoft.tsgui.m5_3.std.models.misc.ITsResources.*;
import static org.toxsoft.tslib.ITsHardConstants.*;
import static org.toxsoft.tslib.av.EAtomicType.*;
import static org.toxsoft.tslib.av.impl.AvUtils.*;
import static org.toxsoft.tslib.av.metainfo.IAvMetaConstants.*;

import java.io.File;

import org.toxsoft.tsgui.m5_3.model.impl.M5AttributeFieldDef;
import org.toxsoft.tsgui.m5_3.model.impl.M5Model;
import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * M5-model of the {@link File} entities.
 *
 * @author hazard157
 */
public class FileM5Model
    extends M5Model<File> {

  /**
   * The model ID.
   */
  public static final String MODEL_ID = TS_ID + ".File"; //$NON-NLS-1$

  /**
   * ID of field {@link #PATH}.
   */
  public static final String FID_PATH = "Path"; //$NON-NLS-1$

  /**
   * ID of field {@link #LENGTH}.
   */
  public static final String FID_LENGTH = "Length"; //$NON-NLS-1$

  /**
   * Attribute {@link File#getName()}.
   */
  public final M5AttributeFieldDef<File> NAME = new M5AttributeFieldDef<>( FID_NAME, STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_FILE_NAME, STR_D_FILE_NAME );
      setDefaultValue( AV_STR_EMPTY );
      setFlags( M5FF_COLUMN );
    }

    protected IAtomicValue doGetFieldValue( File aEntity ) {
      return avStr( aEntity.getName() );
    }

  };

  /**
   * Attribute {@link File#getPath()}.
   */
  public final M5AttributeFieldDef<File> PATH = new M5AttributeFieldDef<>( FID_PATH, STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_FILE_PATH, STR_D_FILE_PATH );
      setDefaultValue( AV_STR_EMPTY );
      setFlags( M5FF_DETAIL );
    }

    protected IAtomicValue doGetFieldValue( File aEntity ) {
      return avStr( aEntity.getPath() );
    }
  };

  /**
   * Attribute {@link File#length()}.
   */
  public final M5AttributeFieldDef<File> LENGTH = new M5AttributeFieldDef<>( FID_LENGTH, INTEGER ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_FILE_LENGTH, STR_D_FILE_LENGTH );
      setDefaultValue( AV_0 );
      setFlags( M5FF_COLUMN );
      params().setValueIfNull( TSID_FORMAT_STRING, avStr( "%,d" ) ); //$NON-NLS-1$
    }

    protected IAtomicValue doGetFieldValue( File aEntity ) {
      return avInt( aEntity.length() );
    }

  };

  /**
   * Constructor.
   */
  public FileM5Model() {
    super( MODEL_ID, File.class );
    setNameAndDescription( STR_N_M5M_FILE, STR_D_M5M_FILE );
    addFieldDefs( NAME, PATH, LENGTH );
  }

  /**
   * Constructor for subclasses.
   * <p>
   * Does not adds any field definition to the model.
   *
   * @param aModelId String - the model ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException model ID is not an IDpath
   */
  public FileM5Model( String aModelId ) {
    super( aModelId, File.class );
    setNameAndDescription( STR_N_M5M_FILE, STR_D_M5M_FILE );
  }

}
