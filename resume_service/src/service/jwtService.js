import jsonwebtoken from 'jsonwebtoken';

class JwtService{
    constructor(){
        this.SECRET= process.env.SECRET;
    }

    extractUserId(token){
        const claims = this.extractClaim(token);
        return claims.sub;
    }

    extractClaim(token){
        try{
            return jsonwebtoken.verify(token, this.SECRET);
        }catch(err){
            console.error('Invalid token:', err.message);
            return null;
        }
    }

    isTokenExpired(token){
        const claims= this.extractClaim(token);
        if(!claims) return true;
        const now= Math.floor(Date.now()/1000);
        return claims.exp < now;
    }

    extractExpiration(token){
        const claims= this.extractClaim(token);
        return claims ? new Date(claims.exp * 1000) : null;
    }
}

export default new JwtService();