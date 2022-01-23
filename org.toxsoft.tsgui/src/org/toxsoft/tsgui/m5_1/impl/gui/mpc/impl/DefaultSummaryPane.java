package org.toxsoft.tsgui.m5_1.impl.gui.mpc.impl;

import static ru.toxsoft.tsgui.m5.gui.multipane.impl.ITsResources.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.toxsoft.tsgui.bricks.tstree.ITsNode;
import org.toxsoft.tsgui.m5_1.impl.gui.mpc.IMpcSummaryPane;
import org.toxsoft.tsgui.utils.layout.BorderLayout;
import org.toxsoft.tsgui.widgets.TsComposite;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

import ru.toxsoft.tslib.utils.collections.basis.ITsReferenceCollection;

/**
 * Реализация {@link IMpcSummaryPane} по умолчанию, отображает текст, поставленный
 * {@link IMessageProvider#messageText(BasicMultiPaneComponent, ITsNode, ITsReferenceCollection)}.
 * <p>
 * По умолчанию показывает количество элементов
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public class DefaultSummaryPane<T>
    extends AbstractPane<T, TsComposite>
    implements IMpcSummaryPane<T> {

  /**
   * Инетрфейс поставщиа текст сообщения дефолтной панели суммарной информации.
   *
   * @author goga
   * @param <T> - класс моделированной сущности
   */
  public interface IMessageProvider<T> {

    /**
     * Реализация должна поставить текст сообщения.
     *
     * @param aOwner {@link BasicMultiPaneComponent} - родительская компонентя
     * @param aSelectedNode {@link ITsNode} - текуий виделенный узел в дереве, может быть <code>null</code>
     * @param aAllItems {@link ITsReferenceCollection}&lt;T&gt; - все (неотфильтрованные) элементы
     * @return String - текст сообщения
     */
    String messageText( BasicMultiPaneComponent<T> aOwner, ITsNode aSelectedNode, ITsReferenceCollection<T> aAllItems );

  }

  /**
   * Поставщик сообщения по умолчанию - пишет общее или общее/отфильтрованное количество элементов.
   */
  @SuppressWarnings( "rawtypes" )
  public static final IMessageProvider DEFAULT_MESSAGE_RPOVIDER = ( aOwner, aSelectedNode, aAllItems ) -> {
   String msg;
   if( aOwner.tree().filterManager().isFiltered() ) {
  msg = String.format( FMT_MSG_ITEMS_FILTERED_COUNT, Integer.valueOf( aAllItems.size() ),
      Integer.valueOf( aOwner.tree().filterManager().items().size() ) );
   }
   else {
  msg = String.format( FMT_MSG_ITEMS_COUNT, Integer.valueOf( aAllItems.size() ) );
   }
   return msg;
  };

  private IMessageProvider<T> messageProvider = DEFAULT_MESSAGE_RPOVIDER;

  private Label label = null;

  /**
   * Конструктор для наследников.
   *
   * @param aOwner {@link BasicMultiPaneComponent} - компонента, создающая эту панель
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   */
  public DefaultSummaryPane( BasicMultiPaneComponent<T> aOwner ) {
    super( aOwner );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов базового класса
  //

  @Override
  protected TsComposite doCreateControl( Composite aParent ) {
    TsComposite c = new TsComposite( aParent, SWT.BORDER );
    c.setLayout( new BorderLayout() );
    label = new Label( c, SWT.LEFT );
    label.setLayoutData( BorderLayout.CENTER );
    return c;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ISummaryPane
  //

  @Override
  public void updatePane( ITsNode aSelectedNode, ITsReferenceCollection<T> aAllItems ) {
    String msg = messageProvider.messageText( owner(), aSelectedNode, aAllItems );
    label.setText( msg );
  }

  // ------------------------------------------------------------------------------------
  // API класса
  //

  /**
   * Возвращает поставщик сообщений.
   *
   * @return {@link IMessageProvider} - поставщик сообщений, не бывает null
   */
  public IMessageProvider<T> getMessageProvider() {
    return messageProvider;
  }

  /**
   * Задает поставщик сообщений.
   *
   * @param aMessageProvider {@link IMessageProvider} - поставщик сообщений
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   */
  public void setMessageProvider( IMessageProvider<T> aMessageProvider ) {
    TsNullArgumentRtException.checkNull( aMessageProvider );
    messageProvider = aMessageProvider;
  }

}
