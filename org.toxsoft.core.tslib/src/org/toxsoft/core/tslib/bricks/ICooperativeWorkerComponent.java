package org.toxsoft.core.tslib.bricks;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * A component in a container that realizes cooperative multitasking.
 * <p>
 * This interface does not introduce new methods, but only combines two parent interfaces -
 * {@link ICooperativeMultiTaskable} and {@link IWorkerComponent}.
 *
 * @author hazard157
 */
public interface ICooperativeWorkerComponent
    extends ICooperativeMultiTaskable, IWorkerComponent {

  // TODO TRANSLATE

  /**
   * Метод из родительского интерфейса {@link ICooperativeMultiTaskable}.
   * <p>
   * Дополнительные особенности метода при работе в контейнере:
   * <p>
   * Предполагается, что для контейнера существует основной поток выпонения. Основной поток в в цикле вызывает этот
   * метод всех компонент контейнера.
   * <p>
   * Вместо этого метода, запуск нового потока выпонения для длительной задачи хотя и разрешается в общем случае, но
   * весьма не рекомендуется, лучше использовать сторонный пул потоков выполненения (есть такой шабон программирования -
   * как называется? не помню).
   * <p>
   * Наличие такого механизма как {@link ICooperativeMultiTaskable#doJob()}, позволяет не создавать отдельные потоки в
   * компонентах, которые периодически делают небольшую работу (например, проверяют, истек ли таймер и генерируют какое
   * либо сообщение).
   *
   * @throws TsIllegalStateRtException работа компоненты была завершена
   */
  @Override
  void doJob();

  // Не имеет собственных методов, только родительские

}
