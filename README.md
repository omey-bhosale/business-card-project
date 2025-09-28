# **Business Card Generator Web Application – Technical Architecture & Analysis**

## **1. Executive Summary**

This project is a full-stack web application enabling business professionals to create, manage, and share digital business cards. Each card integrates social media, contact, and payment links, and is accessible via a unique public URL and QR code. The platform supports secure user authentication (email/password and OTP), profile management, and file uploads (logos, QR codes).

---

## **2. High-Level Architecture (HLD)**

### **System Overview**

![alt text](https://github.com/omey-bhosale/business-card-project/blob/main/business-profile-app/business-card-HHD.png?raw=true)

### **Key Components**

- **Frontend:** Angular 17 SPA, responsive, communicates via REST API.
- **Backend:** Spring Boot REST API, handles business logic, authentication, and file uploads.
- **Database:** MySQL, stores user, profile, and card data.
- **File Storage:** Local filesystem or AWS S3 for images/QR codes.
- **Authentication:** JWT-based stateless authentication.
- **QR Code Generation:** (Planned) ZXing or similar library.
- **PDF/Image Generation:** (Planned) iText or similar library.

---

## **3. Features Implemented**

### **Authentication & User Management**

- Email/password registration and login (AuthService, UserController)
- Phone number OTP verification (UserService)
- JWT-based session management (JwtUtil, JwtAuthenticationFilter)
- Up to 3 business profiles per user (BusinessProfileService)

### **Business Card Generation**

- Form-driven profile creation (CreateProfileComponent)
- Upload logo and payment QR code (onFileSelected)
- Social media, contact, and map links (BusinessProfile)
- Unique public URL for each card (publicUrl)
- QR code upload (QR code generation planned)
- Card preview and sharing (ViewProfileComponent)

### **Integration Points**

- Click-to-call, WhatsApp, Google Reviews, Facebook, Instagram, LinkedIn, Maps, YouTube, payment QR (view-profile.component.html)

### **Technical**

- Responsive UI (Tailwind CSS, custom SCSS)
- Secure file upload with validation (FileUploadService)
- CORS, CSRF, and security best practices (SecurityConfig)
- RESTful API structure

---

## **4. Areas for Improvement**

### **Security**

- **Password Recovery:** Not yet implemented.
- **OAuth2:** Only JWT is implemented; OAuth2 (Google, etc.) can be added.
- **Rate Limiting:** No brute-force protection on login/OTP endpoints.
- **Public Card Security:** Public URLs are predictable; consider using UUIDs or slugs.

### **Features**

- **PDF/Image Export:** Not yet implemented; add iText or similar for downloadable cards.
- **QR Code Generation:** Currently supports upload; should auto-generate QR codes linking to public URLs.
- **Profile Customization:** Allow users to choose card themes/templates.
- **Analytics:** Track card views/clicks.
- **Notifications:** Email/SMS notifications for registration, OTP, etc.

### **Technical**

- **Testing:** Add more unit/integration tests for backend and frontend.
- **Error Handling:** Improve user-facing error messages and backend logging.
- **Scalability:** Move file storage to AWS S3 for production.
- **Accessibility:** Audit and improve accessibility (a11y) in UI.

---

## **5. Features to Add**

- **Password recovery/reset flow**
- **OAuth2 login (Google, LinkedIn, etc.)**
- **Auto QR code generation for each card**
- **PDF/image export of business card**
- **Analytics dashboard for card views/interactions**
- **Customizable card templates/themes**
- **Admin panel for user/profile management**
- **Rate limiting and brute-force protection**
- **Multi-language support**

---

## **6. High-Level Design (HLD)**

### **Frontend**

- **Angular SPA** with routing, guards, and modular components.
- **State:** JWT stored in localStorage, user state managed in services.
- **Forms:** Reactive forms for registration, login, and profile creation.
- **API Communication:** HttpClient with JWT in Authorization header.

### **Backend**

- **Spring Boot REST API**
- **Controllers:** User, Profile, File Upload endpoints.
- **Services:** Business logic, file handling, OTP, authentication.
- **Security:** JWT filter, CORS, CSRF disabled, stateless sessions.
- **Persistence:** JPA repositories for User and BusinessProfile.
- **File Upload:** Multipart endpoints, local/AWS S3 storage.

## **7. Low-Level Design (LLD)**

### **Key Entities**

### User

- id, name, email, phone, password (hashed), otp, isVerified, businessProfiles

### BusinessProfile

- id, businessName, description, logoUrl, phone, email, address, social links, googleMapLink, paymentQrUrl, publicUrl, etc.

### **API Endpoints**

### **API Endpoints**

| Endpoint | Method | Auth | Description |
| --- | --- | --- | --- |
| /api/users/register | POST | No | Register user |
| /api/users/send-otp | POST | No | Send OTP to phone |
| /api/users/verify-otp | POST | No | Verify OTP |
| /api/users/login-email | POST | No | Login with email/password |
| /api/users/upload | POST | Yes | Upload logo/QR code |
| /api/profiles | POST | Yes | Create business profile |
| /api/profiles | GET | Yes | List user’s business profiles |
| /api/profiles/{id} | GET | Yes | Get profile by ID |

### **Frontend Routes**

| Route | Guarded | Description |
| --- | --- | --- |
| / | No | Landing page |
| /sign-in | No | Login |
| /sign-up | No | Registration |
| /dashboard | Yes | User dashboard |
| /create-profile | Yes | Create business profile |
| /profile/:id | No | Public profile/card view |

## **8. Deployment & DevOps**

- **Frontend:** Deployed to AWS S3 (see .github/workflows/deploy-angular.yml)
- **Backend:** Deployed to AWS EC2 (see .github/workflows/deploy-backend.yml)
- **CI/CD:** GitHub Actions for build and deployment

---

## **9. Recommendations & Next Steps**

- **Implement missing features:** Password recovery, QR/PDF generation, analytics.
- **Enhance security:** Add OAuth2, rate limiting, and improve public URL security.
- **Improve UX:** Add card templates, better error handling, and accessibility.
- **Scale file storage:** Move to AWS S3 for production.
- **Testing:** Increase test coverage for both frontend and backend.

---

## **10. References**

- Angular 17 Documentation
- Spring Boot Documentation
- JWT Introduction
- AWS S3 Documentation
- ZXing QR Code Library
- iText PDF Library

---

[https://viewer.diagrams.net/?border=0&tags={}&lightbox=1&highlight=0000ff&edit=_blank&layers=1&nav=1&dark=auto#R<mxfile><diagram name%3D"Business Card Generator HLD" id%3D"htjOp_8EKu6McF4TbfgM">1ZjbcpswEIafxjPpRTOAjMCXjg9p2mQmCe34WsBymADyyCK2%2B%2FRdAvhQkTGZYpPe2OJfSUifVrsSAzJJN7eCLaMH7kMyMDR%2FMyDTgWGMLIq%2FhbAtBUpJKYQi9ktJ3wtO%2FBsqUavUPPZhdVRRcp7IeHksejzLwJNHGhOCr4%2BrBTw5fuuShaAIjscSVV3EvoxK1Tasvf4N4jCq36zTUWlJWV25mskqYj5fH0hkNiATwbksS%2BlmAknBruZStpu%2FY90NTEAm2zSYpeZ0%2BEPPJsvojmRMcjZ1vhrDsptXluTVjMdZmCdMoDgXPJOQ%2BVi8ch7HX6p5yG0NR%2FA886HoXx%2BQm3UUS3CWzCusa%2FQG1CKZJpU5iJNkwhMu3toSn4EdeKivpOAvcGChng1ugJZqaCAkbN6dtL5DiS4IPAUptlilakAr%2BJX36Xb1vN6v5U6LDtaxbscq9wl3Pe8JY6GC%2FBHgpgLcWYo4C1G7KXzB0J5nzk%2F8Gz%2FedQocdN8Eqwn4iFqE0W6AE%2BM0cUO7KHGqEH%2FYOk%2F3KE2ZZC5bgcIZ9%2BqyKHrbJEbg4sOwgyAwvEbv9qlLzY5gU62FexsXhW0psOcxUjU0R3JRxFqMJfcc4yt2P144hYF0HFhMsP1hE3rbcAk9k58TrXc%2FtxX0v1ZQxPGrBbjY5QN3cSlU2LWzp5uwSOHXuCNi7zpftXH77kM0VTnSi2IcKYAwBY6LowQ%2BuQn3XpoQgK%2BcI%2F4CgF3yXHjQYgklEyHIk2lbRSogYTJ%2BPR5J54iI1iOiYVtEZq%2BI9B4RmW0R0V4RGf8DIqtXREQJ6d8Xb2fDHGMjpk4Qr7GnHl%2F%2BJX0GtgfNJxfXNoemdp702RD1L5s9yfCD3uizVbRD3EeAK52jN9c0Pwmveo989oSg3kKenrGnx%2Bkc1VvIQOAoeHauXX36PtKwQJ1eTppOyJe9nBDrk%2Fhs6%2FRDzpSh8XH%2F4enNdvD1jsz%2BAA%3D%3D<%2Fdiagram><%2Fmxfile>](https://viewer.diagrams.net/?border=0&tags=%7B%7D&lightbox=1&highlight=0000ff&edit=_blank&layers=1&nav=1&dark=auto#R%3Cmxfile%3E%3Cdiagram%20name%3D%22Business%20Card%20Generator%20HLD%22%20id%3D%22htjOp_8EKu6McF4TbfgM%22%3E1ZjbcpswEIafxjPpRTOAjMCXjg9p2mQmCe34WsBymADyyCK2%2B%2FRdAvhQkTGZYpPe2OJfSUifVrsSAzJJN7eCLaMH7kMyMDR%2FMyDTgWGMLIq%2FhbAtBUpJKYQi9ktJ3wtO%2FBsqUavUPPZhdVRRcp7IeHksejzLwJNHGhOCr4%2BrBTw5fuuShaAIjscSVV3EvoxK1Tasvf4N4jCq36zTUWlJWV25mskqYj5fH0hkNiATwbksS%2BlmAknBruZStpu%2FY90NTEAm2zSYpeZ0%2BEPPJsvojmRMcjZ1vhrDsptXluTVjMdZmCdMoDgXPJOQ%2BVi8ch7HX6p5yG0NR%2FA886HoXx%2BQm3UUS3CWzCusa%2FQG1CKZJpU5iJNkwhMu3toSn4EdeKivpOAvcGChng1ugJZqaCAkbN6dtL5DiS4IPAUptlilakAr%2BJX36Xb1vN6v5U6LDtaxbscq9wl3Pe8JY6GC%2FBHgpgLcWYo4C1G7KXzB0J5nzk%2F8Gz%2FedQocdN8Eqwn4iFqE0W6AE%2BM0cUO7KHGqEH%2FYOk%2F3KE2ZZC5bgcIZ9%2BqyKHrbJEbg4sOwgyAwvEbv9qlLzY5gU62FexsXhW0psOcxUjU0R3JRxFqMJfcc4yt2P144hYF0HFhMsP1hE3rbcAk9k58TrXc%2FtxX0v1ZQxPGrBbjY5QN3cSlU2LWzp5uwSOHXuCNi7zpftXH77kM0VTnSi2IcKYAwBY6LowQ%2BuQn3XpoQgK%2BcI%2F4CgF3yXHjQYgklEyHIk2lbRSogYTJ%2BPR5J54iI1iOiYVtEZq%2BI9B4RmW0R0V4RGf8DIqtXREQJ6d8Xb2fDHGMjpk4Qr7GnHl%2F%2BJX0GtgfNJxfXNoemdp702RD1L5s9yfCD3uizVbRD3EeAK52jN9c0Pwmveo989oSg3kKenrGnx%2Bkc1VvIQOAoeHauXX36PtKwQJ1eTppOyJe9nBDrk%2Fhs6%2FRDzpSh8XH%2F4enNdvD1jsz%2BAA%3D%3D%3C%2Fdiagram%3E%3C%2Fmxfile%3E)
