package com.turmaa2018.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.turmaa2018.domain.Task;

@WebServlet(name = "task", urlPatterns = "/tasks/*")
public class TasksServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final List<Task> taskList = new ArrayList<Task>();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TasksServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json); charset=UTF-8");

		String pathInfo = request.getPathInfo();
		
		if (pathInfo == null || pathInfo.equals("/")) {
			doGetAll(request, response);
		} else {
			doGetById(request, response, pathInfo);
		}
	}

	private void doGetAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Gson gson = new Gson();
		String json = gson.toJson(taskList);

		response.getWriter().write(json);
	}

	private void doGetById(HttpServletRequest request, HttpServletResponse response, String pathInfo)
			throws ServletException, IOException {
		String[] splits = pathInfo.split("/");
		if (splits.length != 2) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		String taskId = splits[1];
		Task task = findTaskById(taskId);
		if (task == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().write("");
		} else {
			Gson gson = new Gson();
			String json = gson.toJson(task);
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().write(json);
		}
	}

	private Task findTaskById(String taskId) {
		Optional<Task> result = taskList.stream().filter(task -> task.getId().equals(taskId)).findFirst();
		if (!result.isPresent()) {
			return null;
		}
		return result.get();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json; charset=UTF-8");
		Gson gson = new Gson();
		Task task = gson.fromJson(request.getReader(), Task.class);

		taskList.add(task);
		response.setStatus(HttpServletResponse.SC_CREATED);
		String json = gson.toJson(task);
		response.getWriter().write(json);
	}
	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("aplication/json; charset-UTF=8");
		String pathInfo = request.getPathInfo();
		String[] splits = pathInfo.split("/");
		if (splits.length != 2) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		Gson gson = new Gson();
		Task newTask = gson.fromJson(request.getReader(), Task.class);
		String taskId = splits[1];
		Task task = findTaskById(taskId);
		if (task == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().write("");
			return;
		}
		
		task.setTitle(newTask.getTitle());
		task.setResume(newTask.getResume());
		task.setIsDone(newTask.getIsDone());
		String json = gson.toJson(task);
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write(json);
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("aplication/json; charset-UTF=8");
		 String pathInfo = request.getPathInfo();
		 String[] splits = pathInfo.split("/");
		 if(splits.length != 2) {
		 response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		 return;
		 }
		 String taskId = splits[1];
		 Task task = findTaskById(taskId);
		 if (task == null) {
		 response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		 response.getWriter().write("");
		 return;
		 }
		 taskList.remove(task);
		 response.setStatus(HttpServletResponse.SC_OK);
		 response.getWriter().write("");
	}

}
