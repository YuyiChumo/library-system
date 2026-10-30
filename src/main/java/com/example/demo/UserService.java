package com.example.demo;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    UserRepository userRepository;

    public UserService(UserRepository UserRepository) { this.userRepository = UserRepository; }

    public User checkUser(String username, String rawPassword) {
        if (username == null || rawPassword == null) return null;
        User dbUser = userRepository.findByUsername(username);
        if (dbUser == null || !isPasswordCorrect(dbUser, rawPassword)) return null;
        return dbUser;
    }

    boolean isPasswordCorrect(User dbUser, String rawPassword) {
        return dbUser != null && dbUser.getPassword().equals(rawPassword);
    }

    boolean isPasswordCorrect(String username, String password) {
        return username != null && userRepository.findByUsername(username).getPassword().equals(password);
    }

    boolean isPasswordCorrect(User user) {
        if (user == null || user.getUsername() == null) {
            return false;
        }
        return isPasswordCorrect(userRepository.findByUsername(user.getUsername()), user.getPassword());
    }

    // 校验操作人：账号存在、密码正确、并且是管理员
    // 失败时把原因放在 message 里，调用方直接返回给前端即可
    public Result<Boolean> checkAdmin(String username, String rawPassword) {
        if (username == null || rawPassword == null) {
            return Result.fail("账号或密码不能为空", false);
        }
        User dbUser = userRepository.findByUsername(username);
        if (dbUser == null) {
            return Result.fail("用户不存在", false);
        }
        if (!isPasswordCorrect(dbUser, rawPassword)) {
            return Result.fail("密码错误", false);
        }
        if (!Boolean.TRUE.equals(dbUser.getIsAdmin())) {
            return Result.fail("不是管理员，无权操作", false);
        }
        return Result.success("验证通过", true);
    }

    // 添加验证，确认是本人操作
    // 返回 false 表示失败，返回 true 表示成功
    public Result<Boolean> setName(User user, String newName) {
        // 1. 基础校验
        if (user == null || user.getUsername() == null) {
            return Result.fail("用户信息不完整", false);
        }
        if (newName == null || newName.trim().isEmpty()) {
            return Result.fail("新名字不能为空", false);
        }

        // 2. 身份验证
        if (!isPasswordCorrect(user)) {
            return Result.fail("密码错误，无权修改", false);
        }

        // 3. 执行更新
        User dbUser = userRepository.findByUsername(user.getUsername());
        dbUser.setName(newName);
        userRepository.save(dbUser); // save 既可以新增，也可以更新
        return Result.success("名字更改成功", true);
    }

    public Result<Boolean> setPassword(User user, String newPassword, String repeatNewPassword) {
        // 1. 基础校验
        if (user == null || user.getUsername() == null) {
            return Result.fail("用户信息不完整", false);
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return Result.fail("新密码不能为空", false);
        }
        if (!newPassword.equals(repeatNewPassword)) {
            return Result.fail("2次新密码不一致", false);
        }

        // 2. 身份验证
        if (!isPasswordCorrect(user)) {
            return Result.fail("密码错误，无权修改", false);
        }

        // 3. 执行更新
        User dbUser = userRepository.findByUsername(user.getUsername());
        dbUser.setPassword(newPassword);
        userRepository.save(dbUser);
        return Result.success("密码更改成功", true);
    }

    public Result<Boolean> signUp(UserRequest request) {
        User user = request.getUser();
        // 1. 基础校验
        if (user == null || user.getUsername() == null || user.getPassword() == null || user.getName() == null) {
            return Result.fail("用户信息不完整", false);
        }
        // 2. 查重：看用户名是否已被占用
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return Result.fail("用户名已被注册", false);
        }

        // 3. 保存：new 一个新的 User，只复制需要的字段，忽略前端传进来的 id
        User newUser = new User();
        newUser.setName(user.getName().trim());
        newUser.setUsername(user.getUsername().trim());
        newUser.setPassword(user.getPassword());
        newUser.setIsAdmin(user.getIsAdmin());
        userRepository.save(newUser);
        return Result.success("注册成功", true);
    }

    public Result<Boolean> signIn(UserRequest request) {
        User user = request.getUser();
        if (user == null || user.getUsername() == null || user.getPassword() == null) {
            return Result.fail("用户信息不完整", false);
        }
        // 1. 查库
        User dbUser = userRepository.findByUsername(user.getUsername());
        if (dbUser == null) {
            return Result.fail("用户不存在", false);
        }
        // 2. 对比密码
        if (!isPasswordCorrect(dbUser, user.getPassword())) {
            return Result.fail("密码错误", false);
        }
        return Result.success("登录成功", true);
    }

}