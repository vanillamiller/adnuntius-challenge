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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 *  Server
 */
@WebServlet(urlPatterns = { "/" })
public class MedicationServlet extends HttpServlet {

	/**
	 * 
	 * @param msList list of medications that have been added to the repo in this request
	 * @return
	 */
	public String generateGetSuccessResponse(List<Medication> msList) {
		JsonArray msListSerialized = new JsonArray();

		for (Medication ms : msList) {
			msListSerialized.add(ms.serialize());
		}

		System.out.println(msListSerialized);
		JsonObject json = new JsonObject();
		json.add("medicationsSuccessfullyUploaded", msListSerialized);
		return json.toString();
	}

	/**
	 *  accepts no arguments and returns Json with fields:
	 * 		- if no medications in repository: {message : there are currently no medications}
	 * 		- else 
	 * 			- totalMedicationsStored
	 * 			- totalDosagesPerMedication
	 * 			- numberMedicationPerSize
	 *          - numberMedicationPerMedication
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		MedicationRepository medicationRepository = MedicationRepository.getInstance();
		response.getWriter().print(medicationRepository.serialize());
		response.getWriter().close();
	}


	/**
	 *  facilitates both posting formats and returns an array containing the json representations of the
	 * 	medicines uploaded
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			// read the request body json
			StringBuffer jb = new StringBuffer();
			String line = null;

			BufferedReader reader = request.getReader();

			while ((line = reader.readLine()) != null)
				jb.append(line);

			reader.close();
			JsonObject postReqBody = JsonParser.parseString(jb.toString()).getAsJsonObject();

			// get the repo singleton
			MedicationRepository medicationRepository = MedicationRepository.getInstance();

			// if there is not medication strng key, throw exception
			if (postReqBody.get("medicationStrings") == null) {
				throw new Exception("must have parameter 'medicationStrings' for valid post");
			
			// if the post request was made with format 2
			} else if (postReqBody.get("medicationStrings").toString().matches("\\[.*\\]")) {
				JsonArray rawMedicationArray = postReqBody.get("medicationStrings").getAsJsonArray();
				ArrayList<Medication> msList = MedicationFactory.create(rawMedicationArray);

				for (Medication ms : msList) {
					medicationRepository.commitMedication(ms);
				}

				response.getWriter().print(generateGetSuccessResponse(msList));

			// if the post schema matches format 1
			} else {
				String rawMedication = postReqBody.get("medicationStrings").getAsString();
				System.out.println(rawMedication);
				ArrayList<Medication> msList = MedicationFactory.create(rawMedication);
				for (Medication ms : msList) {
					medicationRepository.commitMedication(ms);
				}
				response.getWriter().print(generateGetSuccessResponse(msList));
			}
		
		// if at any point an exception was thrown (most likely MedicalStringFormatException) give 
		// back error message and 403 statusCode
		} catch (Exception e) {
			response.setStatus(403);
			JsonObject errJson = new JsonObject();
			errJson.addProperty("message", e.getMessage());
			response.getWriter().print(errJson.toString());
		
		// close the writer
		} finally {
			response.getWriter().close();
			
		}

	}
}