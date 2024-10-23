package org.toxsoft.core.tslib.bricks.validator.vrl;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Items of {@link IVrList} contains single validation result {@link #vr()} with additional information {@link #info()}.
 *
 * @author hazard157
 * @param vr {@link ValidationResult} - the single validation result
 * @param info {@link IOptionSet} - application specific additional information
 */
public record VrlItem ( ValidationResult vr, IOptionSet info ) {

  /**
   * Singleton of item corresponding to {@link ValidationResult#SUCCESS} with an empty {@link #info()}.
   */
  public static final VrlItem OK = new VrlItem( ValidationResult.SUCCESS );

  /**
   * Constructor.
   * <p>
   * If <code>info</code> argument is <code>null</code> the {@link #info()} field will have value
   * {@link IOptionSet#NULL}.
   * <p>
   * Warning: constructor does <b>not</b> creates defensive copy of <code>info</code> argument, caller must ensure to
   * specify new instance when invoking constructor.
   *
   * @param vr {@link ValidationResult} - the single validation result
   * @param info {@link IOptionSet} - application specific additional information or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VrlItem( ValidationResult vr, IOptionSet info ) {
    TsNullArgumentRtException.checkNull( vr );
    this.vr = vr;

    // DEBUG ---
    if( vr.type() == EValidationResultType.OK ) {
      TsTestUtils.pl( "OK" ); //$NON-NLS-1$
    }
    // ---

    if( info != null ) {
      this.info = info;
    }
    else {
      this.info = IOptionSet.NULL;
    }
  }

  /**
   * Constructor.
   * <p>
   * Same as calling {@link #VrlItem(ValidationResult, IOptionSet)} with <code>info = null</code>
   *
   * @param vr {@link ValidationResult} - the single validation result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VrlItem( ValidationResult vr ) {
    this( vr, null );
  }

}
