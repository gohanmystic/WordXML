package vn.com.fsoft.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import vn.com.fsoft.common.Constants;
import vn.com.fsoft.model.HocSinh;
import vn.com.fsoft.repository.HocSinhRepository;
import vn.com.fsoft.service.HocSinhService;

@Service
public class HocSinhServiceImpl implements HocSinhService, UserDetailsService {

    @Autowired
    HocSinhRepository hocSinhRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HocSinh userInfo = hocSinhRepository.findByUsername(username);

        if (userInfo == null) {
            throw new UsernameNotFoundException("User " + username + " was not found in the database");
        }

        List<GrantedAuthority> grantList= new ArrayList<GrantedAuthority>();
        if (userInfo.getRole() != null) {
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_"+userInfo.getRole().getName());
            grantList.add(authority);
        }

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(userInfo.getUsername(),
                userInfo.getPassword(), grantList);

        return userDetails;
    }

    @Override
    public HocSinh findByUsername(String username) {
        return hocSinhRepository.findByUsername(username);
    }

	@Override
	public HocSinh saveHocSinh(HocSinh hocSinh) {

		if (hocSinh != null) {
			hocSinh.setRoleId(Constants.ROLE_USER);
		}

		return hocSinhRepository.save(hocSinh);
	}

    @Override
    public List<HocSinh> getAllHocSinh() {
        List<HocSinh> listTmp = hocSinhRepository.findAll();
        if (listTmp == null || listTmp.isEmpty()) {
            return new ArrayList<HocSinh>();
        }
        int iSize = listTmp.size();
        List<HocSinh> listResult = new ArrayList<HocSinh>();
        for (int i = 0; i < iSize; i++) {
            if (listTmp.get(i).getRoleId() == Constants.ROLE_USER)
                listResult.add(listTmp.get(i));
        }
        return listResult;
    }

    @Override
    public void deleteHocSinh(Integer maHocSinh) {
        if (hocSinhRepository.exists(maHocSinh))
            hocSinhRepository.delete(maHocSinh);
    }

    @Override
    public HocSinh findByMaHocSinh(Integer maHocSinh) {
        return hocSinhRepository.findOne(maHocSinh);
    }
}
