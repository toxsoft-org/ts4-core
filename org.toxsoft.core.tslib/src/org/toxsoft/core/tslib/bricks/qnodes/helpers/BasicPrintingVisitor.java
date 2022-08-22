package org.toxsoft.core.tslib.bricks.qnodes.helpers;

import java.io.*;

import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.qnodes.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.*;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * QNodes tree visitor simply prints helpful information to the specified output stream.
 * <p>
 * This is a helper class. Helper classis not used by QNodes package, rather it is kind of design pattern how to
 * implement frequently needed functionality related to QNodes.
 *
 * @author hazard157
 */
public class BasicPrintingVisitor
    extends AbstractQNodeVisitor {

  private final File outFile;

  FileWriter   fw = null;
  IStrioWriter sw = null;

  /**
   * Creates instance for file output.
   *
   * @param aFile {@link File} - output file
   * @throws TsNullArgumentRtException aFile = null
   * @throws TsIoRtException file validation failed
   */
  public BasicPrintingVisitor( File aFile ) {
    TsFileUtils.checkFileAppendable( aFile );
    outFile = aFile;
  }

  /**
   * Creates instance for {@link System#out} output.
   */
  public BasicPrintingVisitor() {
    outFile = null;
  }

  @Override
  protected boolean beforeStartSubtree( IQNode aNode ) {
    try {
      ICharOutputStream chOut = CharOutputStreamSysOut.getInstance();
      if( outFile != null ) {
        fw = new FileWriter( outFile );
        chOut = new CharOutputStreamWriter( fw );
      }
      sw = new StrioWriter( chOut );
    }
    catch( IOException ex ) {
      LoggerUtils.errorLogger().error( ex );
      fw = null;
    }
    return false;
  }

  @Override
  protected boolean doVisitNode( IQNode aNode ) {
    int level = 0;
    IQNode n = aNode;
    while( n.parent() != null ) {
      ++level;
      n = n.parent();
    }
    for( int i = 0; i < level; i++ ) {
      sw.writeAsIs( "  " ); //$NON-NLS-1$
    }
    sw.p( "%s: %s   NODE_PARAMS= ", aNode.kind().id(), aNode.nmName() ); //$NON-NLS-1$
    OptionSetKeeper.KEEPER.write( sw, aNode.nodeData().params() );
    sw.writeEol();
    return false;
  }

  @Override
  protected void afterEndSubtree( IQNode aNode, boolean aWasCancelled, Throwable aException, IQNode aStopperNode ) {
    if( fw != null ) {
      try {
        fw.close();
      }
      catch( IOException ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
      fw = null;
    }
    sw = null;
  }

}
