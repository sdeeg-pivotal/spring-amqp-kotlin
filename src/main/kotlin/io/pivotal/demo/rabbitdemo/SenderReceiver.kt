package io.pivotal.demo.rabbitdemo

import org.springframework.amqp.core.Queue
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.EnableScheduling

class Tut1Sender(val template: RabbitTemplate, val queue: Queue) {

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    fun send() {
        val message = "Hello World!"
        template.convertAndSend(queue.getName(), message)
        println("[x] Sent '$message'")
    }
}

@RabbitListener(queues = arrayOf("hello"))
class Tut1Receiver {

    @RabbitHandler
    fun receive(messageIn: String) {
        println("[x] Received '$messageIn'")
    }
}

@Profile("!usage_message")
@Configuration
@EnableScheduling
class tutorialConfiguration {
    @Bean
    fun hello() = Queue("hello")

    @Profile("receiver")
    @Bean
    fun reciever() = Tut1Receiver()

    @Profile("sender")
    @Bean
    fun sender(template: RabbitTemplate, queue: Queue) = Tut1Sender(template, queue)
}