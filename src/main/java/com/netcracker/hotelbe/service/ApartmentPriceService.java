package com.netcracker.hotelbe.service;

import com.netcracker.hotelbe.entity.ApartmentClass;
import com.netcracker.hotelbe.entity.ApartmentPrice;
import com.netcracker.hotelbe.repository.ApartmentPriceRepository;
import com.netcracker.hotelbe.service.filter.FilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;

@Service
public class ApartmentPriceService {

    @Autowired
    private ApartmentPriceRepository apartmentPriceRepository;

    @Autowired
    private ApartmentClassService apartmentClassService;

    @Autowired
    @Qualifier("apartmentPriceValidator")
    private Validator apartmentPriceValidator;

    @Autowired
    private FilterService filterService;

    @Autowired
    private EntityService entityService;

    public List<ApartmentPrice> findAll() {
        return apartmentPriceRepository.findAll();
    }

    public List<ApartmentPrice> getAllByParams(Map<String, String> allParams) {
        if(allParams.size()!=0) {
            return apartmentPriceRepository.findAll(filterService.fillFilter(allParams, ApartmentPrice.class));
        } else {
            return apartmentPriceRepository.findAll();
        }
    }

    public ApartmentPrice save(ApartmentPrice apartmentPrice) {
        final ApartmentClass apartmentClass = apartmentClassService.findById(apartmentPrice.getApartmentClass().getId());
        apartmentPrice.setApartmentClass(apartmentClass);

        return apartmentPriceRepository.save(apartmentPrice);
    }

    public ApartmentPrice findById(final Long id) {
        return apartmentPriceRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.valueOf(id))
        );
    }

    public ApartmentPrice update(ApartmentPrice apartmentPrice, final Long id) {
        apartmentPriceRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.valueOf(id))
        );

        final ApartmentClass apartmentClass = apartmentClassService.findById(apartmentPrice.getApartmentClass().getId());

        apartmentPrice.setApartmentClass(apartmentClass);
        apartmentPrice.setId(id);

        return apartmentPriceRepository.save(apartmentPrice);
    }

    public void deleteById(final Long id) {
        final ApartmentPrice delete = apartmentPriceRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.valueOf(id))
        );

        apartmentPriceRepository.delete(delete);
    }

    public ApartmentPrice patch(Long id, Map<String, Object> updates) {
        ApartmentPrice apartmentPrice = apartmentPriceRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.valueOf(id))
        );

        return apartmentPriceRepository.save((ApartmentPrice) entityService.fillFields(updates, apartmentPrice));
    }

    public void validate(final ApartmentPrice apartmentPrice, BindingResult bindingResult) throws MethodArgumentNotValidException {
        apartmentPriceValidator.validate(apartmentPrice, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
    }
}
