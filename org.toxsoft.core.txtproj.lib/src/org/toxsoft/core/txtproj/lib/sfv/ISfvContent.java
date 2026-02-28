package org.toxsoft.core.txtproj.lib.sfv;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.notifier.*;
import org.toxsoft.core.txtproj.lib.tdfile.*;

/**
 * Sectioned File View - the content of the file with KTRO sections.
 * <p>
 * This interface, unlike the {@link ITdFile} interface, is intended for examining the contents of a file. The main
 * difference is that this interface allows you to have sections with the same identifiers in a file. Also, this
 * interface manages order of the sections.
 *
 * @author hazard157
 */
public interface ISfvContent
    extends IGenericChangeEventCapable {

  /**
   * Returns all the sections.
   *
   * @return {@link INotifierListEdit}&lt;{@link ISfvSection}&gt; - an editable sections list
   */
  INotifierListEdit<ISfvSection> sections();

  /**
   * Lists all sections with the specified ID in the order of appearance in {@link #sections()}.
   *
   * @param aSectionId String - section ID
   * @return {@link IListEdit}&lt;{@link ISfvSection}&gt; - an editable instance of newly created sections list
   */
  IListEdit<ISfvSection> listSectionsById( String aSectionId );

}
