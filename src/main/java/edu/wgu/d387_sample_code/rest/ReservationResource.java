package edu.wgu.d387_sample_code.rest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import edu.wgu.d387_sample_code.convertor.ReservationService;
import edu.wgu.d387_sample_code.convertor.ReservationServiceImpl;
import edu.wgu.d387_sample_code.convertor.RoomEntityToReservableRoomResponseConverter;
import edu.wgu.d387_sample_code.convertor.RoomService;
import edu.wgu.d387_sample_code.convertor.RoomServiceImpl;
import edu.wgu.d387_sample_code.convertor.TimeConverter;
import edu.wgu.d387_sample_code.entity.ReservationEntity;
import edu.wgu.d387_sample_code.entity.RoomEntity;
import edu.wgu.d387_sample_code.model.request.ReservationRequest;
import edu.wgu.d387_sample_code.model.response.ReservableRoomResponse;
import edu.wgu.d387_sample_code.model.response.ReservationResponse;
//import edu.wgu.d387_sample_code.repository.PageableRoomRepository;
import edu.wgu.d387_sample_code.repository.ReservationRepository;
import edu.wgu.d387_sample_code.repository.RoomRepository;

@RestController
@RequestMapping(ResourceConstants.ROOM_RESERVATION_V1)
@CrossOrigin
public class ReservationResource extends Thread {

	@Autowired
	private ResourceBundleMessageSource source;

	@Autowired
	ApplicationContext context;

	// @Autowired
	// PageableRoomRepository pageableRoomRepository;

	@Autowired
	RoomRepository roomRepository;

	@Autowired
	ReservationRepository reservationRepository;

	@Autowired
	ConversionService conversionService;

	@Autowired
	private RoomEntityToReservableRoomResponseConverter converter;
	List<String> list = new ArrayList<>();
	{

		// add 5 element in ArrayList
		list.add("fr");
		list.add("en");

		// for (int i = 0; i < n; i++) {
		// ReservationResource object = new ReservationResource();
		// object.start();
		// object.run();
		// }

	}
	String language = "en";
	static Locale localeGlobal = null;
	static boolean isTreadStart = false;
	static long threadId = 0;
	static long count = 0;
	static String message = "";

	@RequestMapping(path = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<ReservableRoomResponse> getAvailableRooms(
			@RequestParam(value = "checkin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkin,
			@RequestParam(value = "checkout") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkout,
			Pageable pageable) {

		RoomService roomService = context.getBean(RoomServiceImpl.class);
		ReservationService reservationService = context.getBean(ReservationServiceImpl.class);
		List<RoomEntity> allRooms = roomService.findAll();
		List<ReservationEntity> allReservations = reservationService.findAll();
		for (ReservationEntity reservationEntity : allReservations) {
			LocalDate rcheckin = reservationEntity.getCheckin();
			LocalDate rcheckout = reservationEntity.getCheckout();
			if (rcheckin.isBefore(checkin) && rcheckout.isAfter(checkin))
				allRooms.remove(reservationEntity.getRoomEntity());
			else if (rcheckin.isAfter(checkin) && rcheckin.isBefore(checkout))
				allRooms.remove(reservationEntity.getRoomEntity());
			else if (rcheckin.isEqual(checkin))
				allRooms.remove(reservationEntity.getRoomEntity());
		}
		Page<RoomEntity> page = new PageImpl<>(allRooms);
		return page.map(converter::convert);
	}

	@RequestMapping(path = "/{roomId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RoomEntity> getRoomById(@PathVariable Long roomId) {

		Optional<RoomEntity> result = roomRepository.findById(roomId);
		RoomEntity roomEntity = null;

		if (result.isPresent()) {
			roomEntity = result.get();
		} else {
			// we didn't find the employee
			// throw new RuntimeException("Did not find part id - " + theId);
			return null;
		}

		return new ResponseEntity<>(roomEntity, HttpStatus.OK);
	}

	@RequestMapping(path = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ReservationResponse> createReservation(@RequestBody ReservationRequest reservationRequest) {

		ReservationEntity reservationEntity = conversionService.convert(reservationRequest, ReservationEntity.class);
		reservationRepository.save(reservationEntity);
		ReservationService repository = context.getBean(ReservationServiceImpl.class);
		reservationEntity = repository.findLast();
		Optional<RoomEntity> result = roomRepository.findById(reservationRequest.getRoomId());
		RoomEntity roomEntity = null;

		if (result.isPresent()) {
			roomEntity = result.get();
		} else {
			// we didn't find the employee
			// throw new RuntimeException("Did not find part id - " + theId);
			return null;
		}

		roomEntity.addReservationEntity(reservationEntity);
		roomRepository.save(roomEntity);
		reservationEntity.setRoomEntity(roomEntity);

		ReservationResponse reservationResponse = conversionService.convert(reservationEntity,
				ReservationResponse.class);

		return new ResponseEntity<>(reservationResponse, HttpStatus.CREATED);
		// return new ResponseEntity<>(new ReservationResponse(), HttpStatus.CREATED);
	}

	@RequestMapping(path = "", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ReservableRoomResponse> updateReservation(
			@RequestBody ReservationRequest reservationRequest) {

		return new ResponseEntity<>(new ReservableRoomResponse(), HttpStatus.OK);
	}

	@RequestMapping(path = "/{reservationId}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteReservation(@PathVariable long reservationId) {

		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/time")
	public JSONObject getTime() {
		String utcTime = TimeConverter.currentUTCTime();
		// System.out.println(TimeConverter.dateFormate(utcTime));
		// System.out.println("UTC time: " + TimeConverter.dateTimeToAMPM(utcTime));

		String etTime = TimeConverter.convertUTCToET(utcTime);
		// System.out.println("Eastern Time: " + TimeConverter.dateTimeToAMPM(etTime));

		String mtTime = TimeConverter.convertETToMT(etTime);
		// System.out.println("Mountain Time: " + TimeConverter.dateTimeToAMPM(mtTime));

		String time = "Join us for an online presentation held at the london hotel on "
				+ TimeConverter.dateFormate(utcTime) + " at " + TimeConverter.dateTimeToAMPM(utcTime)
				+ " Eastern Time | " + TimeConverter.dateTimeToAMPM(etTime) + "| Mountain Time | "
				+ TimeConverter.dateTimeToAMPM(mtTime) + " UTC ";
		System.out.println(time);

		JSONObject json = new JSONObject();
		json.put("value", time);
		return json;
	}

	@GetMapping("/message")
	public JSONObject getLocaleMessage() {

		Runnable runnableTask = () -> {
			if (isTreadStart == false) {
				int n = 4; // Number of threads
				for (int i = 0; i < n; i++) {
					ReservationResource object = new ReservationResource();
					object.start();
					object.run();
				}
				isTreadStart = true;
			}
		};
		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
		executorService.execute(runnableTask);
		System.out.println(localeGlobal);

		message += source.getMessage("message", new Object[] { "test" }, localeGlobal) + "! Thread" + threadId + " ";
		if (count >= 5) {
			message = source.getMessage("message", new Object[] { "test" }, localeGlobal) + "! Thread" + threadId + " ";
			count = 0;
		} else {

		}
		count++;
		JSONObject json = new JSONObject();
		json.put("value", message);
		return json;

	}

	public void run() {

		while (true) {
			try {
				localeGlobal = new Locale("fr");
				// Displaying the thread that is running
				Random rand = new Random();
				localeGlobal = new Locale(list.get(rand.nextInt(list.size())));
				System.out.println(localeGlobal);
				// ResourceBundleMessageSource reservationServicess =
				// context.getBean(ResourceBundleMessageSource.class);
				threadId = Thread.currentThread().getId();
				System.out.println("Thread " + Thread.currentThread().getId() + " is running");
				// System.out.println(reservationServicess.getMessage("message", new Object[] {
				// "londom" }, locale));
				Thread.sleep(50000);
				;
			} catch (Exception e) {
				// Throwing an exception
				System.out.println(e);
				System.out.println("Exception is caught");
			}
		}

	}

	public String getRandomElement(List<String> list) {
		Random rand = new Random();
		return list.get(rand.nextInt(list.size()));
	}

}
