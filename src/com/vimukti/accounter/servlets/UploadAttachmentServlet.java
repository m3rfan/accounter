package com.vimukti.accounter.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.HibernateUtil;

public class UploadAttachmentServlet extends BaseServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8883973951774618294L;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Session session = null;
		try {
			String companyID = (String) request.getSession().getAttribute(COMPANY_ID);
			if (companyID == null)
				return;
			session = HibernateUtil.openSession();
			StringBuilder builder = new StringBuilder();
			/*
			 * Mutipart request which upload file in given temp directory refer
			 * to com.oreilly.
			 */
			// String workspaceID=(String) request.getAttribute("workspaceID");

			MultipartRequest multi = new MultipartRequest(request,
					ServerConfiguration.getTmpDir(), 50 * 1024 * 1024,
					"ISO-8859-1", new DefaultFileRenamePolicy());

			// response.getWriter().print("<html><body>");
			// JSONObject obj=new JSONObject();
			Enumeration<?> files = multi.getFileNames();
			// String parentID = (String) request.getParameter("parentId");
			// if (parentID == null) {
			// return;
			// }
			while (files.hasMoreElements()) {
				String fileID = (String) files.nextElement();
				File file = multi.getFile(fileID);
				if (file != null) {
					String fileName = file.getName();
					// int size = (int) file.length();
					// if (size == 0) {
					// FileWriter stream = new FileWriter(file);
					// stream.write(" ");
					// stream.close();
					//
					// }
					// size = (int) file.length();
					// // String filepath = file.getPath();
					// if (fileID == null || fileID.equals("")) {
					// fileID = SecureUtils.createID();
					// }

					File attachmentDir = new File(
							ServerConfiguration.getAttachmentsDir() + "/"
									+ companyID);
					if (!attachmentDir.exists()) {
						attachmentDir.mkdirs();
					}

					/*
					 * ClientAttachment fileInfo = new ClientAttachment(
					 * fileName, size, parentID + fileID);
					 */
					// file.renameTo(new File(attachmentDir + File.separator
					// + fileName));

					FileOutputStream fout = new FileOutputStream(attachmentDir
							+ File.separator + fileName);
					// ObjectOutputStream objout = new ObjectOutputStream(fout);
					// objout.writeObject(fileName);
					// objout.writeInt(size);
					// objout.writeObject(file);
					// objout.close();
					// fout.close();
					// IDocumentStore doc = new ServerDocumentStore();
					// IAttachment attachmet = doc.createDocument(fileID);
					response.setContentType("text/html");
					// builder.append(getJSonAttachment(attachmet));
					builder.append(fileName);

					InputStream in = new FileInputStream(file);
					OutputStream out = fout;
					byte[] buf = new byte[1024];
					int len;
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
					in.close();
					out.close();
					// System.out.println("File copied.");
				}

			}
			response.getWriter().print(builder);
			// jsonObj.put("fileIDS", fileArray);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

}
