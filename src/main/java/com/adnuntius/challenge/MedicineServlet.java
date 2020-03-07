package com.adnuntius.challenge;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

// import org.json.JSONObject;
@WebServlet(urlPatterns = { "/" })
public class MedicineServlet extends HttpServlet {

	public String generateGetSuccessResponse(List<MedicineString> msList) {
		JsonArray msListSerialized = new JsonArray();

		for (MedicineString ms : msList) {
			msListSerialized.add(ms.serialize());
		}

		System.out.println(msListSerialized);
		JsonObject json = new JsonObject();
		json.add("MedicationsSuccessfullyUploaded", msListSerialized);
		return json.toString();
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		MedicineRepository medicineRepository = MedicineRepository.getInstance();
		response.getWriter().print(medicineRepository.serialize());
		response.getWriter().close();
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			StringBuffer jb = new StringBuffer();
			String line = null;

			BufferedReader reader = request.getReader();

			while ((line = reader.readLine()) != null)
				jb.append(line);

			JsonObject postReqBody = JsonParser.parseString(jb.toString()).getAsJsonObject();

			MedicineRepository medicineRepository = MedicineRepository.getInstance();

			if (postReqBody.get("medicationStrings") == null) {
				throw new Exception("must have parameter 'medicationStrings' for valid post");

			} else if (postReqBody.get("medicationStrings").toString().matches("\\[.*\\]")) {
				JsonArray rawMedicineArray = postReqBody.get("medicationStrings").getAsJsonArray();
				ArrayList<MedicineString> msList = new ArrayList<MedicineString>();

				for (JsonElement rms : rawMedicineArray) {
					msList.add(new MedicineString(rms.getAsString()));
				}

				for (MedicineString ms : msList) {
					medicineRepository.commitMedicineString(ms);
				}

				response.getWriter().print(generateGetSuccessResponse(msList));

			} else {
				String rawMedicineString = postReqBody.get("medicationStrings").getAsString();
				System.out.println(rawMedicineString);
				ArrayList<MedicineString> msList = MedicineStringFactory.create(rawMedicineString);
				for (MedicineString ms : msList) {
					medicineRepository.commitMedicineString(ms);
				}
				response.getWriter().print(generateGetSuccessResponse(msList));
			}

		} catch (Exception e) {
			response.setStatus(403);
			JsonObject errJson = new JsonObject();
			errJson.addProperty("message", e.getMessage());
			response.getWriter().print(errJson.toString());
		} finally {
			response.getWriter().close();

		}

	}
}