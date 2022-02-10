package org.toxsoft.core.tsgui.rcp.valed;

import static org.toxsoft.core.tsgui.rcp.valed.ITsResources.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.widgets.FileDialog;
import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.impl.DataDef;
import org.toxsoft.core.tslib.av.metainfo.IDataDef;
import org.toxsoft.core.tslib.bricks.keeper.std.StringListKeeper;
import org.toxsoft.core.tslib.coll.primtypes.IStringList;

/**
 * Filesystem objects editing valeds constants.
 *
 * @author hazard157
 */
public interface IValedFileConstants {

  /**
   * Prefix of all option IDs in this interface.
   */
  String OP_PREFIX_ID = VALED_OPID_PREFIX + ".File"; //$NON-NLS-1$

  /**
   * ID of option {@link #OPDEF_IS_DIRECTORY}.
   */
  String OPID_IS_DIRECTORY = OP_PREFIX_ID + ".Directory"; //$NON-NLS-1$

  /**
   * ID of option {@link #OPDEF_MUST_EXIST}.
   */
  String OPID_MUST_EXIST = OP_PREFIX_ID + ".MustExist"; //$NON-NLS-1$

  /**
   * ID of option {@link #OPDEF_IS_OPEN_DIALOG}.
   */
  String OPID_IS_OPEN_DIALOG = OP_PREFIX_ID + ".IsOpenDialog"; //$NON-NLS-1$

  /**
   * ID of option {@link #OPDEF_FILE_EXTENSIONS}.
   */
  String OPID_FILE_EXTENSIONS = OP_PREFIX_ID + ".FileExtensions"; //$NON-NLS-1$

  /**
   * Признак выбора директория, не файла.<br>
   * Option type: {@link EAtomicType#BOOLEAN}<br>
   * Usage: <code>true</code> - filesystem object must be a directory, <code>false</code> - a file<br>
   * Default value: <code>false</code> (select file, not directory)
   */
  IDataDef OPDEF_IS_DIRECTORY = DataDef.create( OPID_IS_DIRECTORY, BOOLEAN, //
      TSID_NAME, STR_D_IS_DIRECTORY, //
      TSID_DESCRIPTION, STR_N_IS_DIRECTORY, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  /**
   * File of folder must exist.<br>
   * Type: {@link EAtomicType#BOOLEAN}<br>
   * Usage: <code>true</code> - for non-existing files {@link ValedFile#getValue()} will throw exception<br>
   * Default value: <code>false</code> any file/folder name will be accepted
   */
  IDataDef OPDEF_MUST_EXIST = DataDef.create( OPID_MUST_EXIST, BOOLEAN, //
      TSID_NAME, STR_D_MUST_EXIST, //
      TSID_DESCRIPTION, STR_N_MUST_EXIST, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  /**
   * Признак использования диалога открытия (а не сохранения) файла.<br>
   * Type: {@link EAtomicType#BOOLEAN}<br>
   * Usage: <code>true</code> - on button click will be shown file open (not save) dialog<br>
   * Default value: <code>false</code> (file save dialog)
   */
  IDataDef OPDEF_IS_OPEN_DIALOG = DataDef.create( OPID_IS_OPEN_DIALOG, BOOLEAN, //
      TSID_NAME, STR_D_IS_OPEN_DIALOG, //
      TSID_DESCRIPTION, STR_N_IS_OPEN_DIALOG, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  /**
   * File extensions used in file dialog.<br>
   * Тип: {@link EAtomicType#VALOBJ} of type {@link IStringList}<br>
   * Usage: extension are specified in form "*.ext" (more prcisely as for
   * {@link FileDialog#setFilterExtensions(String[])}). Empty list means any extension.<br>
   * Default value: {@link IStringList#EMPTY} - any extensions
   */
  IDataDef OPDEF_FILE_EXTENSIONS = DataDef.create( OPID_FILE_EXTENSIONS, VALOBJ, //
      TSID_NAME, STR_D_FILE_EXTENSIONS, //
      TSID_DESCRIPTION, STR_N_FILE_EXTENSIONS, //
      TSID_KEEPER_ID, StringListKeeper.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( IStringList.EMPTY ) );

}
