package com.wangsan.study.ssm;

import java.util.EnumSet;
import java.util.Map;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.annotation.EventHeaders;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

/**
 * created by wangsan on 2020/3/24 in project of example .
 *
 * @author wangsan
 * @date 2020/3/24
 */
public class HelloSsm {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        context.register(Config1.class);
        context.refresh();

        StateMachine stateMachine = context.getBean(StateMachine.class);
        stateMachine.start();
        stateMachine.sendEvent(Events.EVENT1);
        stateMachine.sendEvent(Events.EVENT2);
        stateMachine.sendEvent(Events.EVENT3);
    }

    static enum States {
        STATE1, STATE2, STATE3, STATE4
    }

    static enum Events {
        EVENT1, EVENT2, EVENT3, EVENT4
    }

    @Configuration
    @EnableStateMachine
    static class Config1 extends EnumStateMachineConfigurerAdapter<States, Events> {

        @Override
        public void configure(StateMachineStateConfigurer<States, Events> states)
                throws Exception {
            states.withStates()
                    .initial(States.STATE1)
                    .states(EnumSet.allOf(States.class));
        }

        @Override
        public void configure(StateMachineTransitionConfigurer<States, Events> transitions)
                throws Exception {
            transitions.withExternal()
                    .source(States.STATE1).target(States.STATE2)
                    .event(Events.EVENT1)
                    .action(action1())
                    .and()
                    .withExternal()
                    .source(States.STATE2).target(States.STATE1)
                    .event(Events.EVENT2)
                    .action(action2())
                    .and()
                    .withExternal()
                    .source(States.STATE1).target(States.STATE3)
                    .event(Events.EVENT3)
                    .action(new Action<States, Events>() {
                        @Override
                        public void execute(StateContext<States, Events> context) {
                            System.out.println(String.format("from %s to %s with ctx %s",
                                                             context.getSource().getId(),
                                                             context.getTarget().getId(),
                                                             context.getMessage()));

                            Message<Events> message = MessageBuilder
                                    .withPayload(Events.EVENT4)
                                    .setHeader("eee", "eeee")
                                    .build();
                            context.getStateMachine().sendEvent(message);
                        }
                    })
                    .and()
                    .withExternal()
                    .source(States.STATE3).target(States.STATE4)
                    .event(Events.EVENT4);

        }

        @Bean
        public Action<States, Events> action1() {
            return new Action<States, Events>() {
                @Override
                public void execute(StateContext<States, Events> context) {
                    context.getExtendedState().getVariables().put("foo", "bar");
                }
            };
        }

        @Bean
        public Action<States, Events> action2() {
            return new Action<States, Events>() {
                @Override
                public void execute(StateContext<States, Events> context) {
                    System.err.println(context.getExtendedState().getVariables().get("foo"));
                    context.getExtendedState().getVariables().put("foo", "bar2");
                }
            };
        }

        @WithStateMachine
        static class MyBean {

            @OnTransition(target = "STATE1")
            public void toState1() {
                System.out.println("here STATE1");
            }

            @OnTransition(target = "STATE4")
            public void toState2(StateMachine sm, ExtendedState state, Message message,
                                 @EventHeaders Map<String, Object> map) {
                System.out.println("that " + state);
                System.out.println("that " + message);
                System.out.println("that " + map);
            }
        }

    }

}

