package prs.midwit.PetaProject.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import prs.midwit.PetaProject.member.domain.entity.Card;
import prs.midwit.PetaProject.member.domain.repo.CardRepository;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;


    public void save(Card newCard) {
        cardRepository.save(newCard);
    }
}
