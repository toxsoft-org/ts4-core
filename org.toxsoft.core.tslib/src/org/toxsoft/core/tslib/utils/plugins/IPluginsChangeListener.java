package org.toxsoft.core.tslib.utils.plugins;

/**
 * Интерфейс слушателя изменений в файлах плагинов.
 * 
 * @author hazard157
 */
public interface IPluginsChangeListener {

  /**
   * "Нулевой" слушатель, используется вместо null.
   */
  IPluginsChangeListener NULL = new InternalNullPluginsChangeListener();

  /**
   * За время,прошедшее с последней проверки, были изменения в плагинах.
   * 
   * @param aChangedPluginsInfo IChangedPluginsInfo - описание изменений в плагинах
   */
  void onPluginsChanged( IChangedPluginsInfo aChangedPluginsInfo );

}

final class InternalNullPluginsChangeListener
    implements IPluginsChangeListener {

  @Override
  public void onPluginsChanged( IChangedPluginsInfo changedPluginsInfo ) {
    // ничекго не делает
  }

}
