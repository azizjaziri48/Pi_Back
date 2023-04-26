package com.example.pi_back.config;
/*
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

	/*public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req,HttpServletResponse res) throws AuthenticationException {
	    try {
	        ApplicationUser creds = null;

	        if (req.getParameter("username") != null  && req.getParameter("password") != null) {
	            creds = new ApplicationUser();              
	            creds.setUsername(req.getParameter("username"));
	            creds.setPassword(req.getParameter("password"));                
	        } else {
	            creds = new ObjectMapper()
	                    .readValue(req.getInputStream(), ApplicationUser.class);
	        }

	        return getAuthenticationManager().authenticate(
	                new UsernamePasswordAuthenticationToken(
	                        creds.getUsername(),
	                        creds.getPassword(),
	                        new ArrayList<>())
	        );
	    } catch (IOException e) {
	        throw new RuntimeException(e);
	    }
	}*/
