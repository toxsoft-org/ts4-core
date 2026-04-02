package org.toxsoft.core.tslib.bricks;

import org.toxsoft.core.tslib.utils.errors.*;

// TODO TRANSLATE

/**
 * A component that performs its work under the control of a container.
 * <p>
 * Этот интерфейс примешивается (mixin interface) к основному, <b><i>рабочему интерфейсу</i></b> бизнес-логики
 * компоненты. Рабочий интерфейс (или класс) является тем, с чем работает пользователь компоненты, а этот интерфейс
 * всего лишь является особенностью реализации.
 * <p>
 * Любая компонента (модуль, класс), который должен инициализироваться, работать и завершать работу в контексте своего
 * контейнера, может расширять данный интерфейс.
 * <p>
 * Жизненный цикл модуля (компоненты) состоит из:
 * <ul>
 * <li>начало работы - метод {@link #start()};</li>
 * <li>обработка запросов - любые методы рабочего интерфейса, реализованные в наследниках;</li>
 * <li>(опционально) периодическая работа - метод {@link ICooperativeMultiTaskable#doJob()}, вызывается контейнером во
 * время бездействия системы (состояние idle). Этот метод реализует кооперативную многозадачность компонент в
 * контейнере;</li>
 * <li>завершение работы - запрос на завершение {@link #queryStop()}, ожидание завершения {@link #isStopped()},
 * принудительное окончание работы и освобождение всех ресурсов {@link #destroy()}.</li>
 * </ul>
 * Остальные аспекты работы компоненты определяется конкретным типом компоненты контейнера. В частности, наследники
 * определяют поведение компоненты:
 * <ul>
 * <li>разрешен ли повторный запуск компоненты после окончания работы (когда {@link #isStopped()}=true) (после вызова
 * {@link #destroy()} работа компоненты запрещена);</li>
 * <li>разрешено ли создание нескольких экземпляров одного класса компоненты;</li>
 * <li>способ идентификации компонент в контейнере (только ссылки на java-объекты, уникальные идентификаторы и
 * т.п.);</li>
 * <li>может ли компонента запускать свои потоки исполнения. Не рекомендованная практика, предпочитительно осуществить
 * кооперативную (добровольную) многозадачность методом {@link ICooperativeMultiTaskable#doJob()}. При этом, контейнер
 * может создавать свой поток выполнения, и оттуда вызывать {@link ICooperativeMultiTaskable#doJob()} компоненты;</li>
 * <li>а также любые другие аспекты работы контейнера/компоненты.</li>
 * </ul>
 *
 * @author hazard157
 */
public interface IWorkerComponent {

  /**
   * The component start - after this method component is in working state.
   * <p>
   * Only after executing this method can you call the component's business logic methods, as well as
   * {@link ICooperativeMultiTaskable#doJob()}, if the class also implements {@link ICooperativeMultiTaskable}.
   * <p>
   * Calling this method on started component does nothing.
   *
   * @throws TsIllegalStateRtException component is in illegal state (eg. already destroyed)
   * @throws TsRuntimeException - the exception based on {@link TsRemoteIoRtException} may be thrown for many reasons
   */
  void start();

  /**
   * Initializes component stopping process.
   * <p>
   * The method returns immediately, even if the unit is not stopped. In this case, the stopping state should be queried
   * using the {@link #isStopped()} method.
   * <p>
   * Calling this method on stopped or stopping component does nothing.
   * <p>
   * Implementation must <b>not</b> throw any exception except listed below..
   *
   * @return <b>true</b> - the component has stopped working, {@link #isStopped()} = <code>true</code>;<br>
   *         <b>false</b> - component is still working, continue with {@link #isStopped()} calls until stop.
   * @throws TsIllegalStateRtException component is in illegal state (eg. already destroyed)
   */
  boolean queryStop();

  /**
   * Determines if component is stopped.
   * <p>
   * The container guarantees that this method is called only after {@link #queryStop()}, and only if the component is
   * not stopped. Once the component is stopped (that is, {@link #isStopped()} = <code>true</code>), this method is no
   * longer called.
   * <p>
   * If a component takes too long to stop voluntarily, the container should forcefully call {@link #destroy()} to
   * terminate the work (maybe not very correctly) and free up resources.
   * <p>
   * Implementation must <b>not</b> throw any exception except listed below..
   *
   * @return <b>true</b> - the component has stopped working, {@link #isStopped()} = <code>true</code>;<br>
   *         <b>false</b> - component is still working, continue with {@link #isStopped()} calls until stop.
   * @throws TsIllegalStateRtException component is in illegal state (eg. already destroyed)
   */
  boolean isStopped();

  /**
   * Unconditionally terminates the job and releases all resources.
   * <p>
   * Must be called after the component has finished working, or if the component has been waiting too long for
   * completion, after a timeout. This method must be called before the component reference is given to the garbage
   * collector.
   * <p>
   * Once this method is called, no other method can or should be called and the reference to the component should be
   * garbage collected.
   * <p>
   * Implementation must <b>not</b> throw any exception.
   */
  void destroy();

}
