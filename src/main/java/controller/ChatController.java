package controller;

import dto.ChatMensajeDTO;
import model.Mensaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import service.IMensajeService;

@Controller
public class ChatController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate; //este se usa para enviar los mensajes a los clientes
    @Autowired
    private IMensajeService mensajeService;

    @MessageMapping("/chat/{matchId}")
    public void handleChatMessage(@DestinationVariable Long matchId, @Payload ChatMensajeDTO chatMensajeDTO) {        try{
        chatMensajeDTO.setMatchId(matchId);
        Mensaje mensajeGuardado = mensajeService.guardarMensaje(chatMensajeDTO);

        ChatMensajeDTO respuestaDTO = new ChatMensajeDTO();
        respuestaDTO.setContenido(mensajeGuardado.getContenido());
        respuestaDTO.setEmisorId(mensajeGuardado.getEmisor().getId());
        respuestaDTO.setEmisorNombre(mensajeGuardado.getEmisor().getNombre());
        respuestaDTO.setMatchId(mensajeGuardado.getMatch().getId());

        String destination = "/topic/match/" + matchId;
        messagingTemplate.convertAndSend(destination, respuestaDTO);
        System.out.println("mensaje reenviado a : " + destination);


        }catch (Exception e){
            System.err.println("Error procesando mensaje para match " + matchId + ": " + e.getMessage());
            String errorDestination = "/user/" + chatMensajeDTO.getEmisorNombre() + "/queue/errors";
            messagingTemplate.convertAndSend(errorDestination,"error al renviar: "+ e.getMessage());
        }

    }
}
