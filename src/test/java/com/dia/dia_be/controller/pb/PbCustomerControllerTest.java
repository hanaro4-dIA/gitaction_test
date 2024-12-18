package com.dia.dia_be.controller.pb;

import static org.assertj.core.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.dia.dia_be.dto.pb.customerDTO.ResponseCustomerDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
// 실제 서버 포트에서 실행 & 실제 DB 사용 문제가 된다면 다른 DB나 여기서 BeforeEach로 수정할게요!
@Transactional
@AutoConfigureMockMvc
public class PbCustomerControllerTest {

	@Autowired
	private MockMvc mockMvc;

	private final String baseUrl = "http://localhost:8080/pb/customers"; // 기본으로 묶인 URL
	// private final String baseUrl = "http://localhost:8080/1/customers"; // 기본으로 묶인 URL

	//  GET {{base_url}}/pb/customers/list
	@Test
	void testGetCustomerList() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/list"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();

		String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();

		ResponseCustomerDTO[] customerList = objectMapper.readValue(responseBody, ResponseCustomerDTO[].class);

		//전체 길이 테스트
		for (int i = 0; i < customerList.length; i++) {
			ResponseCustomerDTO customerDTO = customerList[i];

			assertThat(customerDTO.getId()).isEqualTo(i + 1);
			assertThat(customerDTO.getName()).isNotEmpty();
			assertThat(customerDTO.getEmail()).isNotEmpty();
		}

		ResponseCustomerDTO customerDto = customerList[0];

		assertThat(customerDto.getName()).isEqualTo("강재준");
		assertThat(customerDto.getPbId()).isEqualTo(1L); // Pb_id
		assertThat(customerDto.getEmail()).isEqualTo("email1@example.com");
		assertThat(customerDto.getPassword()).isEqualTo("password1");
		assertThat(customerDto.getAddress()).isEqualTo("서울특별시 강남구");
		assertThat(customerDto.getTel()).isEqualTo("010-9945-5020");
		assertThat(customerDto.getCount()).isEqualTo(10);
		assertThat(customerDto.getMemo()).isEqualTo("강남구 거주, 안정적 자산 관리 필요.");
		assertThat(customerDto.getDate()).isEqualTo(LocalDate.of(2023, 10, 1));
	}

	//  GET {{base_url}}/pb/customers/search?name={{customerName}}
	@Test
	void testSearchCustomer() throws Exception {
		String name = "강재준";

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/search")
				.param("name", name))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();

		String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

		assertThat(responseBody).contains(name);
	}

	//  GET {{base_url}}/pb/customers/list/{{customerId}}
	@Test
	void testGetCustomerDetail() throws Exception {
		long customerId = 1L;
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/list/" + customerId))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();

		String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();

		ResponseCustomerDTO customerDTO = objectMapper.readValue(responseBody, ResponseCustomerDTO.class);

		assertThat(customerDTO.getId()).isEqualTo(1L);
		assertThat(customerDTO.getPbId()).isEqualTo(1L);
		assertThat(customerDTO.getName()).isEqualTo("강재준");
		assertThat(customerDTO.getPassword()).isEqualTo("password1");
		assertThat(customerDTO.getEmail()).isEqualTo("email1@example.com");
		assertThat(customerDTO.getTel()).isEqualTo("010-9945-5020");
		assertThat(customerDTO.getAddress()).isEqualTo("서울특별시 강남구");
		assertThat(customerDTO.getDate()).isEqualTo(LocalDate.parse("2023-10-01"));
		assertThat(customerDTO.getCount()).isEqualTo(10);
		assertThat(customerDTO.getMemo()).isEqualTo("강남구 거주, 안정적 자산 관리 필요.");
	}

	//POST {{base_url}}/pb/customers/{{customerId}}/memo
	@Test
	void testUpdateCustomerMemo() throws Exception {
		long customerId = 1L;
		String newMemo = "새로운 메모 내용";

		String requestJson = String.format("{\"memo\": \"%s\"}", newMemo);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/" + customerId + "/memo")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();

		String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
		System.out.println(responseBody);

		assertThat(responseBody).contains(newMemo);
	}

}
